package by.lupach.questionnaireportal.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}