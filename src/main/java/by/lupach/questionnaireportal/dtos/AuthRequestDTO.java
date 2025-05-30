package by.lupach.questionnaireportal.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Authentication request containing user credentials")
public class AuthRequestDTO {

    @Schema(description = "User email address", example = "user@example.com", required = true)
    private String email;

    @Schema(description = "User password", example = "P@ssw0rd", required = true)
    private String password;
}
