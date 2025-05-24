package by.lupach.questionnaireportal.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "DTO representing a questionnaire with its fields")
public class QuestionnaireDTO {

    @Schema(description = "Unique identifier of the questionnaire", example = "1")
    private Long id;

    @Schema(description = "Name/title of the questionnaire", example = "Customer Satisfaction Survey")
    private String name;

    @Schema(description = "Detailed description of the questionnaire", example = "This survey aims to collect customer feedback...")
    private String description;

    @Schema(description = "List of fields/questions in the questionnaire")
    private List<FieldDTO> fields;
}
