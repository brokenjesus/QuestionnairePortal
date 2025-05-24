package by.lupach.questionnaireportal.dtos;

import by.lupach.questionnaireportal.models.FieldType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Data Transfer Object representing a Field in a questionnaire")
public class FieldDTO {

    @Schema(description = "Unique identifier of the field", example = "1")
    private Long id;

    @Schema(description = "Label or question text for the field", example = "What is your age?")
    private String label;

    @Schema(description = "Type of the field", example = "TEXT")
    private FieldType type;

    @Schema(description = "Indicates if the field is mandatory", example = "true")
    private boolean isRequired;

    @Schema(description = "Indicates if the field is currently active", example = "true")
    private boolean isActive;

    @Schema(description = "List of options for selectable fields, empty for free text fields")
    private List<String> options;
}
