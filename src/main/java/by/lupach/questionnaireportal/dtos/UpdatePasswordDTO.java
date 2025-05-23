package by.lupach.questionnaireportal.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordDTO {
    @NotBlank
    private String currentPassword;
    @NotBlank
    private String newPassword;
}