package by.lupach.questionnaireportal.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseFieldAnswerDTO {
    private Long fieldId;
    private String answer;
}