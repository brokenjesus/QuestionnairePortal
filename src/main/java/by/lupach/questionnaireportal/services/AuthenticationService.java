package by.lupach.questionnaireportal.services;

import by.lupach.questionnaireportal.dtos.*;
import by.lupach.questionnaireportal.exceptions.InvalidCurrentPasswordException;
import by.lupach.questionnaireportal.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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


    public void signUp(SignUpRequestDTO request) {
        String verificationCode = verificationCodeService.generateVerificationCode(request.getEmail());

        emailService.sendVerificationEmail(request.getEmail(), verificationCode);

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .enabled(false)
                .build();

        userService.create(user);
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

        if (!passwordEncoder.matches(updatePasswordDTO.getCurrentPassword(), user.getPassword())) {
            throw new InvalidCurrentPasswordException("Current newPassword doesn't match");
        }

        user.setPassword(passwordEncoder.encode(updatePasswordDTO.getNewPassword()));
        userService.save(user);
        emailService.sendPasswordSuccessfullyChangedMessage(user.getEmail(), user.getFirstName());
    }


    public void updatePassword(ForgotPasswordDTO forgotPasswordDTO) {
        User user = userService.getByEmail(forgotPasswordDTO.getEmail());

        user.setPassword(passwordEncoder.encode(forgotPasswordDTO.getNewPassword()));
        userService.save(user);
        emailService.sendPasswordSuccessfullyChangedMessage(user.getEmail(), user.getFirstName());
    }


    public void updatePasswordVerifyEmail(UserDetails userDetails){
        String email = userDetails.getUsername();
        changePasswordSendVerificationEmail(email);
    }

    public void changePasswordSendVerificationEmail(String email) {
        String code = verificationCodeService.generateVerificationCode(email);
        emailService.sendVerificationEmail(email, code);
    }


    public User getCurrentUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.getByEmail(username);
    }
}