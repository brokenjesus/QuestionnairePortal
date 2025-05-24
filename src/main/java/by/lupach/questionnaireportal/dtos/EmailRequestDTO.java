package by.lupach.questionnaireportal.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request DTO containing email for password recovery or similar purposes")
public class EmailRequestDTO {

    @Schema(description = "User email address", example = "user@example.com", required = true)
    private String email;
}
