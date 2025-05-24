package by.lupach.questionnaireportal.controllers;

import by.lupach.questionnaireportal.dtos.PageResponseDTO;
import by.lupach.questionnaireportal.dtos.ResponseAnswerDTO;
import by.lupach.questionnaireportal.dtos.ResponsesRequestDTO;
import by.lupach.questionnaireportal.services.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@Tag(name = "WebSocket Responses",
        description = "WebSocket endpoints for real-time response data streaming")
public class ResponseWebSocketController {

    private final ResponseService responseService;

    public ResponseWebSocketController(ResponseService responseService) {
        this.responseService = responseService;
    }

    @Operation(
            summary = "Get paginated responses",
            description = "Subscribe to get paginated responses for a questionnaire via WebSocket. " +
                    "Send a message to '/app/responses/get' and listen on '/topic/responses'.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Request parameters for paginated responses",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ResponsesRequestDTO.class)
                    )
            )
    )
    @MessageMapping("/responses/get")
    @SendTo("/topic/responses")
    public PageResponseDTO<ResponseAnswerDTO> getResponsesByQuestionnaire(
            ResponsesRequestDTO request) {
        return responseService.getResponsesDataPaginated(
                request.getQuestionnaireId(),
                request.getPage(),
                request.getSize()
        );
    }
}