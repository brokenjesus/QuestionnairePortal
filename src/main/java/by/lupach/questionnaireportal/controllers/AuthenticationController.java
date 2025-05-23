package by.lupach.questionnaireportal.controllers;

import by.lupach.questionnaireportal.dtos.*;
import by.lupach.questionnaireportal.exceptions.InvalidCurrentPasswordException;
//import by.lupach.questionnaireportal.security.JwtTokenProvider;
import by.lupach.questionnaireportal.services.AuthenticationService;
import by.lupach.questionnaireportal.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
//    private final JwtTokenProvider jwtTokenProvider;
//    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;
    private final EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequestDTO request) {
        try {
            AuthResponseDTO responseDTO = authenticationService.logIn(request);
            return ResponseEntity.ok(responseDTO);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid email or password");
        }
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestBody VerifyEmailRequest request) {
        try {
            boolean isVerified = authenticationService.verifyEmail(request.getEmail(), request.getCode());

            if (isVerified) {
                return ResponseEntity.ok("User registered successfully");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid verification code or code has expired");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Email verification failed: " + e.getMessage());
        }
    }


    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignUpRequestDTO request) {
        authenticationService.signUp(request);

        return ResponseEntity.ok("User registered successfully");
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody @Valid UpdateProfileDTO updateProfileDTO) {
        authenticationService.updateProfile(updateProfileDTO);

        return ResponseEntity.ok("Profile updated successfully");
    }



    @PutMapping("/change-password")
    public ResponseEntity<?> updatePassword(@RequestBody @Valid UpdatePasswordDTO updatePasswordDTO) {
        try {
            authenticationService.updatePassword(updatePasswordDTO);
            return ResponseEntity.ok(Map.of("message", "Password updated successfully"));
        } catch (InvalidCurrentPasswordException exception) {
            return ResponseEntity.badRequest().body(Map.of("message", exception.getMessage()));
        }
    }

    @GetMapping("/change-password/send-verification-code")
    public ResponseEntity<?> sendVerificationCode(@AuthenticationPrincipal UserDetails userDetails) {
        authenticationService.updatePasswordVerifyEmail(userDetails);
        return ResponseEntity.ok("Verification code sent");
    }
}