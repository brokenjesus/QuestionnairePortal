package by.lupach.questionnaireportal.services;

import by.lupach.questionnaireportal.dtos.*;
import by.lupach.questionnaireportal.models.Questionnaire;
import by.lupach.questionnaireportal.models.Response;
import by.lupach.questionnaireportal.models.ResponseFieldAnswer;
import by.lupach.questionnaireportal.repositories.FieldRepository;
import by.lupach.questionnaireportal.repositories.QuestionnaireRepository;
import by.lupach.questionnaireportal.repositories.ResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

        Map<String, Object> updatedData = getResponsesData(dto.getQuestionnaireId());
        messagingTemplate.convertAndSend("/topic/responses", updatedData);

        return response;
    }

    public List<Response> getResponsesByQuestionnaireId(Long questionnaireId) {
        return responseRepository.findByQuestionnaireIdWithAnswers(questionnaireId);
    }

    public Map<String, Object> getResponsesData(Long questionnaireId) {
        List<Response> respons = getResponsesByQuestionnaireId(questionnaireId);
        QuestionnaireDTO questionnaire = questionnaireService.getById(questionnaireId);

        Map<String, Object> result = new HashMap<>();
        result.put("questionnaireId", questionnaireId);
        result.put("questionnaireTitle", questionnaire.getName());

        List<String> fields = questionnaire.getFields().stream()
                .map(FieldDTO::getLabel)
                .collect(Collectors.toList());
        result.put("fields", fields);

        List<Map<String, String>> tableData = respons.stream().map(response -> {
            Map<String, String> row = new HashMap<>();
            response.getAnswers().forEach(answer -> {
                row.put(answer.getField().getLabel(), answer.getAnswer());
            });
            return row;
        }).collect(Collectors.toList());

        result.put("data", tableData);
        return result;
    }

    public PageResponseDTO<ResponseAnswerDTO> getResponsesDataPaginated(Long questionnaireId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Response> responsePage = responseRepository.findByQuestionnaireIdWithAnswers(questionnaireId, pageable);

        List<ResponseAnswerDTO> content = responsePage.getContent().stream().map(response -> {
            Map<String, String> answersMap = response.getAnswers().stream()
                    .collect(Collectors.toMap(
                            a -> a.getField().getLabel(),
                            ResponseFieldAnswer::getAnswer
                    ));
            return new ResponseAnswerDTO(answersMap);
        }).collect(Collectors.toList());

        PageResponseDTO<ResponseAnswerDTO> pageResponse = new PageResponseDTO<>();
        pageResponse.setContent(content);
        pageResponse.setPageNumber(responsePage.getNumber());
        pageResponse.setPageSize(responsePage.getSize());
        pageResponse.setTotalElements(responsePage.getTotalElements());
        pageResponse.setTotalPages(responsePage.getTotalPages());
        pageResponse.setLast(responsePage.isLast());

        return pageResponse;
    }

}
