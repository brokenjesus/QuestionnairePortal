package by.lupach.questionnaireportal.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO representing the user signup request")
public class SignUpRequestDTO {

    @Schema(description = "User email address", example = "user@example.com", required = true)
    private String email;

    @Schema(description = "Password chosen by the user", example = "P@ssw0rd123", required = true)
    private String password;

    @Schema(description = "User first name", example = "John")
    private String firstName;

    @Schema(description = "User last name", example = "Doe")
    private String lastName;

    @Schema(description = "User phone number", example = "+1234567890")
    private String phoneNumber;
}
