package by.lupach.questionnaireportal.services;

import by.lupach.questionnaireportal.dtos.QuestionnaireDTO;
import by.lupach.questionnaireportal.dtos.FieldDTO;
import by.lupach.questionnaireportal.dtos.ResponseDTO;
import by.lupach.questionnaireportal.models.Questionnaire;
import by.lupach.questionnaireportal.models.Response;
import by.lupach.questionnaireportal.models.ResponseFieldAnswer;
import by.lupach.questionnaireportal.repositories.FieldRepository;
import by.lupach.questionnaireportal.repositories.QuestionnaireRepository;
import by.lupach.questionnaireportal.repositories.ResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResponseService {

    private final ResponseRepository responseRepository;
    private final QuestionnaireRepository questionnaireRepository;
    private final FieldRepository fieldRepository;
    private final QuestionnaireService questionnaireService;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public Response saveResponse(ResponseDTO dto) {
        Questionnaire questionnaire = questionnaireRepository.findById(dto.getQuestionnaireId())
                .orElseThrow(() -> new RuntimeException("Questionnaire not found"));

        Response response = new Response();
        response.setQuestionnaire(questionnaire);

        Response finalResponse = response;
        var answers = dto.getAnswers().stream().map(answerDTO -> {
            ResponseFieldAnswer answer = new ResponseFieldAnswer();
            answer.setAnswer(answerDTO.getAnswer());
            answer.setField(fieldRepository.findById(answerDTO.getFieldId())
                    .orElseThrow(() -> new RuntimeException("Field not found")));
            answer.setResponse(finalResponse);
            return answer;
        }).collect(Collectors.toList());

        response.setAnswers(answers);

        response = responseRepository.save(response);

        // После сохранения отправляем обновленные данные всем подписчикам
        Map<String, Object> updatedData = getResponsesData(dto.getQuestionnaireId());
        messagingTemplate.convertAndSend("/topic/responses", updatedData);

        return response;
    }

    public List<Response> getResponsesByQuestionnaireId(Long questionnaireId) {
        return responseRepository.findByQuestionnaireIdWithAnswers(questionnaireId);
    }

    public Map<String, Object> getResponsesData(Long questionnaireId) {
        List<Response> responses = getResponsesByQuestionnaireId(questionnaireId);
        QuestionnaireDTO questionnaire = questionnaireService.getById(questionnaireId); // без author

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
