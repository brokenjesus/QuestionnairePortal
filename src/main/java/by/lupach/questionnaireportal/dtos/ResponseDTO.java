package by.lupach.questionnaireportal.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO representing a user's response to a questionnaire")
public class ResponseDTO {

    @Schema(description = "ID of the questionnaire answered", example = "1")
    private Long questionnaireId;

    @Schema(description = "List of answers given to fields")
    private List<ResponseFieldAnswerDTO> answers;
}
