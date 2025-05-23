package by.lupach.questionnaireportal.services;

import by.lupach.questionnaireportal.dtos.*;
import by.lupach.questionnaireportal.exceptions.InvalidCurrentPasswordException;
import lombok.RequiredArgsConstructor;
import by.lupach.questionnaireportal.models.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final VerificationCodeService verificationCodeService;


    public AuthResponseDTO signUp(SignUpRequestDTO request) {
        // Generate verification code
        String verificationCode = verificationCodeService.generateVerificationCode(request.getEmail());

        // Send email with verification code
        emailService.sendVerificationEmail(request.getEmail(), verificationCode);

        // Save user data temporarily (you might need a temporary storage solution)
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .enabled(false) // User is not enabled until email is verified
                .build();

        userService.create(user);

        return new AuthResponseDTO(null, user.getEmail(), user.getFirstName(),
                user.getLastName(), user.getPhoneNumber());
    }

    public boolean verifyEmail(String email, String code) {
        if (verificationCodeService.validateCode(email, code)) {
            User user = userService.getByEmail(email);
            user.setEnabled(true);
            userService.save(user);
            return true;
        }
        return false;
    }


    public AuthResponseDTO logIn(AuthRequestDTO request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));

        User user = (User) userService
                .userDetailsService()
                .loadUserByUsername(request.getEmail());

        String token = jwtService.generateToken(user);

        return new AuthResponseDTO(token, user.getEmail(), user.getFirstName(), user.getLastName(), user.getPhoneNumber());
    }

    public void updateProfile(UpdateProfileDTO updateProfileDTO) {
        User user = getCurrentUser();

        user.setFirstName(updateProfileDTO.getFirstName());
        user.setLastName(updateProfileDTO.getLastName());
        if (!user.getEmail().equals(updateProfileDTO.getEmail())) {
            user.setEmail(updateProfileDTO.getEmail());
            emailService.sendEmailSuccessfullyChangedMessage(user.getEmail(), user.getFirstName());
        }
        user.setPhoneNumber(updateProfileDTO.getPhoneNumber());

        userService.save(user);
    }


    public void updatePassword(UpdatePasswordDTO updatePasswordDTO) {
        User user = getCurrentUser();

        if (passwordEncoder.encode(user.getPassword()).equals(updatePasswordDTO.getCurrentPassword())) {
            throw new InvalidCurrentPasswordException("Current password doesn't match");
        }

        user.setPassword(passwordEncoder.encode(updatePasswordDTO.getNewPassword()));

        userService.save(user);

        emailService.sendPasswordSuccessfullyChangedMessage(user.getEmail(), user.getFirstName());
    }



    public User getCurrentUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.getByEmail(username);
    }
}