package by.lupach.questionnaireportal.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Response returned after successful authentication")
public class AuthResponseDTO {

    @Schema(description = "JWT token for authenticated user", example = "eyJhbGciOiJIUzI1...")
    private String token;

    @Schema(description = "User email address", example = "user@example.com")
    private String email;

    @Schema(description = "User first name", example = "John")
    private String firstName;

    @Schema(description = "User last name", example = "Doe")
    private String lastName;

    @Schema(description = "User phone number", example = "+1234567890")
    private String phoneNumber;
}
