package by.lupach.questionnaireportal.dtos;

import lombok.Data;

import java.util.List;

@Data
public class QuestionnaireDTO {
    private Long id;
    private String name;
    private String description;
    private List<FieldDTO> fields;
}
