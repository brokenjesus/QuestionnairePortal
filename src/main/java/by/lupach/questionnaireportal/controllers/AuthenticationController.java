package by.lupach.questionnaireportal.controllers;

import by.lupach.questionnaireportal.dtos.*;
import by.lupach.questionnaireportal.exceptions.InvalidCurrentPasswordException;
import by.lupach.questionnaireportal.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Authentication", description = "Endpoints for user authentication and account management")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(schema = @Schema(implementation = AuthResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequestDTO request) {
        try {
            AuthResponseDTO responseDTO = authenticationService.logIn(request);
            return ResponseEntity.ok(responseDTO);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid email or password");
        }
    }

    @Operation(summary = "Verify email", description = "Verify user's email with verification code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email verified successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid verification code"),
            @ApiResponse(responseCode = "500", description = "Email verification failed")
    })
    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestBody VerifyEmailRequest request) {
        try {
            boolean isVerified = authenticationService.verifyEmail(request.getEmail(), request.getCode());

            if (isVerified) {
                return ResponseEntity.ok("User registered successfully");
            } else {
                return ResponseEntity.badRequest().body("Invalid verification code or code has expired");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Email verification failed: " + e.getMessage());
        }
    }

    @Operation(summary = "User registration", description = "Register a new user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignUpRequestDTO request) {
        authenticationService.signUp(request);
        return ResponseEntity.created(null).body("User registered successfully");
    }

    @Operation(summary = "Update profile", description = "Update user profile information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody @Valid UpdateProfileDTO updateProfileDTO) {
        authenticationService.updateProfile(updateProfileDTO);
        return ResponseEntity.ok("Profile updated successfully");
    }

    @Operation(summary = "Change password", description = "Change user password (requires current password)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid current password")
    })
    @PutMapping("/change-password")
    public ResponseEntity<?> updatePassword(@RequestBody @Valid UpdatePasswordDTO updatePasswordDTO) {
        try {
            authenticationService.updatePassword(updatePasswordDTO);
            return ResponseEntity.ok(Map.of("message", "Password updated successfully"));
        } catch (InvalidCurrentPasswordException exception) {
            return ResponseEntity.badRequest().body(Map.of("message", exception.getMessage()));
        }
    }

    @Operation(summary = "Reset password", description = "Reset password (forgot password flow)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid verification code")
    })
    @PutMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody @Valid ForgotPasswordDTO updatePasswordDTO) {
        try {
            authenticationService.updatePassword(updatePasswordDTO);
            return ResponseEntity.ok(Map.of("message", "Password updated successfully"));
        } catch (InvalidCurrentPasswordException exception) {
            return ResponseEntity.badRequest().body(Map.of("message", exception.getMessage()));
        }
    }

    @Operation(summary = "Send verification code for password change",
            description = "Send verification code to email for changing password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verification code sent")
    })
    @GetMapping("/change-password/send-verification-code")
    public ResponseEntity<?> changePasswordSendVerificationCode(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
        authenticationService.updatePasswordVerifyEmail(userDetails);
        return ResponseEntity.ok("Verification code sent");
    }

    @Operation(summary = "Send verification code for password reset",
            description = "Send verification code to email for resetting password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verification code sent")
    })
    @PostMapping("/forgot-password/send-verification-code")
    public ResponseEntity<?> forgotPasswordSendVerificationCode(@RequestBody EmailRequestDTO request) {
        authenticationService.changePasswordSendVerificationEmail(request.getEmail());
        return ResponseEntity.ok("Verification code sent");
    }
}