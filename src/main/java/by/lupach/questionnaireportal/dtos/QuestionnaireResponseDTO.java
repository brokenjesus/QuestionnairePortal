package by.lupach.questionnaireportal.dtos;

import lombok.Data;

import java.util.Map;

@Data
public class QuestionnaireResponseDTO {
    private Map<Long, String> answers; // key = fieldId, value = answer
}
