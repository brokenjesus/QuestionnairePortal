package by.lupach.questionnaireportal.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO for requesting paginated questionnaire responses")
public class ResponsesRequestDTO {

    @Schema(description = "ID of the questionnaire to retrieve responses for", example = "1", required = true)
    private Long questionnaireId;

    @Schema(description = "Page number to retrieve (0-based)", example = "0")
    private Integer page;

    @Schema(description = "Number of responses per page", example = "20")
    private Integer size;
}
