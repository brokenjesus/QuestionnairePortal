package by.lupach.questionnaireportal.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO used for password reset requests")
public class ForgotPasswordDTO {

    @Schema(description = "Email of the user requesting password reset", example = "user@example.com", required = true)
    private String email;

    @Schema(description = "New password to set", example = "NewP@ssw0rd", required = true)
    private String newPassword;
}
