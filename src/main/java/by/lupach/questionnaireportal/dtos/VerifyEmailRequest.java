package by.lupach.questionnaireportal.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyEmailRequest {

    @Schema(description = "Email to verify", example = "user@example.com")
    private String email;

    @Schema(description = "Verification code sent to the email", example = "123456")
    private String code;
}
