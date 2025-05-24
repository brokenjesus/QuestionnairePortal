package by.lupach.questionnaireportal.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO representing answers submitted to a questionnaire")
public class ResponseAnswerDTO {

    @Schema(description = "Map of field labels or IDs to user-provided answers")
    private Map<String, String> answers;
}
