package by.lupach.questionnaireportal.services;

import by.lupach.questionnaireportal.dtos.FieldDTO;
import by.lupach.questionnaireportal.dtos.PageResponseDTO;
import by.lupach.questionnaireportal.dtos.ResponseAnswerDTO;
import by.lupach.questionnaireportal.dtos.ResponseDTO;
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
import java.util.LinkedHashMap;

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

        Page<Response> lastPage = responseRepository.findByQuestionnaireIdWithAnswers(dto.getQuestionnaireId(), PageRequest.of(0, 10));
        PageResponseDTO<ResponseAnswerDTO> updatedData = getResponsesDataPaginated(dto.getQuestionnaireId(), lastPage.getTotalPages() - 1, 10);
        messagingTemplate.convertAndSend("/topic/responses", updatedData);

        return response;
    }

    public PageResponseDTO<ResponseAnswerDTO> getResponsesDataPaginated(Long questionnaireId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Response> responsePage = responseRepository.findByQuestionnaireIdWithAnswers(questionnaireId, pageable);

        List<FieldDTO> allFields = questionnaireService.getById(questionnaireId).getFields();

        List<ResponseAnswerDTO> content = responsePage.getContent().stream().map(response -> {
            Map<String, String> answersMap = allFields.stream()
                    .collect(Collectors.toMap(
                            FieldDTO::getLabel,
                            f -> "",
                            (existing, replacement) -> existing,
                            LinkedHashMap::new
                    ));

            response.getAnswers().forEach(answer -> answersMap.put(answer.getField().getLabel(), answer.getAnswer()));

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
