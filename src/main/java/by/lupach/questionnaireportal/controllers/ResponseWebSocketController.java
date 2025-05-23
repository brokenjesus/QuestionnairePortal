package by.lupach.questionnaireportal.controllers;

import by.lupach.questionnaireportal.dtos.FieldDTO;
import by.lupach.questionnaireportal.dtos.QuestionnaireDTO;
import by.lupach.questionnaireportal.dtos.ResponsesRequestDTO;
import by.lupach.questionnaireportal.models.Response;
import by.lupach.questionnaireportal.models.User;
import by.lupach.questionnaireportal.services.JwtService;
import by.lupach.questionnaireportal.services.QuestionnaireService;
import by.lupach.questionnaireportal.services.ResponseService;
import by.lupach.questionnaireportal.services.UserService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.handler.annotation.Header;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ResponseWebSocketController {

    private final ResponseService responseService;
    private final QuestionnaireService questionnaireService;
    private final UserService userService;
    private final JwtService jwtService;

    public ResponseWebSocketController(ResponseService responseService,
                                       QuestionnaireService questionnaireService, UserService userService, JwtService jwtService) {
        this.responseService = responseService;
        this.questionnaireService = questionnaireService;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @MessageMapping("/responses/get")
    @SendTo("/topic/responses")
    public Map<String, Object> getResponsesByQuestionnaire(
            ResponsesRequestDTO request,
            @Header("Authorization") String authorizationHeader) {

        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        }

        if (token == null) {
            throw new RuntimeException("Authorization token is missing");
        }

        // Получить username из токена
        String username = jwtService.extractUserName(token);
        if (username == null) {
            throw new RuntimeException("Invalid token");
        }

        // Получить пользователя из БД по username
        User author = userService.getByEmail(username);
        if (author == null) {
            throw new RuntimeException("User not found");
        }

        Long questionnaireId = request.getQuestionnaireId();

        List<Response> responses = responseService.getResponsesByQuestionnaireId(questionnaireId);
        QuestionnaireDTO questionnaire = questionnaireService.getByIdAndAuthor(questionnaireId, author);

        Map<String, Object> result = new HashMap<>();
        result.put("questionnaireId", questionnaireId);
        result.put("questionnaireTitle", questionnaire.getName());

        List<String> fields = questionnaire.getFields().stream()
                .map(FieldDTO::getLabel)
                .collect(Collectors.toList());
        result.put("fields", fields);

        List<Map<String, String>> tableData = responses.stream().map(response -> {
            Map<String, String> row = new HashMap<>();
            response.getAnswers().forEach(answer -> {
                row.put(answer.getField().getLabel(), answer.getAnswer());
            });
            return row;
        }).collect(Collectors.toList());

        result.put("data", tableData);

        return result;
    }
}
