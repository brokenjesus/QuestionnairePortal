package by.lupach.questionnaireportal.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO representing a single answer to a questionnaire field")
public class ResponseFieldAnswerDTO {

    @Schema(description = "ID of the field being answered", example = "10")
    private Long fieldId;

    @Schema(description = "Answer text", example = "Yes, I agree")
    private String answer;
}
