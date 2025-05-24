package by.lupach.questionnaireportal.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordDTO {

    @Schema(description = "Current password of the user", example = "OldPass123")
    private String currentPassword;

    @Schema(description = "New password to be set", example = "NewSecurePass456")
    private String newPassword;
}
