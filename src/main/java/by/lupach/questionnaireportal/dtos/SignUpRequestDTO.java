package by.lupach.questionnaireportal.dtos;

import lombok.Data;

@Data
public class SignUpRequestDTO {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
