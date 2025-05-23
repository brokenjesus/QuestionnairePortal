package by.lupach.questionnaireportal.dtos;

import by.lupach.questionnaireportal.models.FieldType;
import lombok.Data;

import java.util.List;

@Data
public class FieldDTO {
    private Long id;
    private String label;
    private FieldType type;
    private boolean isRequired;
    private boolean isActive;
    private List<String> options;
}
