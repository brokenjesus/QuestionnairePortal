package by.lupach.questionnaireportal.services;

import by.lupach.questionnaireportal.dtos.FieldDTO;
import by.lupach.questionnaireportal.dtos.QuestionnaireDTO;
import by.lupach.questionnaireportal.models.Field;
import by.lupach.questionnaireportal.models.Questionnaire;
import by.lupach.questionnaireportal.models.User;
import by.lupach.questionnaireportal.repositories.QuestionnaireRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuestionnaireService {
    private final QuestionnaireRepository questionnaireRepository;
    private final FieldService fieldService;
    private final UserService userService;
    private final AuthenticationService authenticationService;

    public QuestionnaireService(QuestionnaireRepository questionnaireRepository,
                                FieldService fieldService, UserService userService, AuthenticationService authenticationService) {
        this.questionnaireRepository = questionnaireRepository;
        this.fieldService = fieldService;
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    public QuestionnaireDTO create(QuestionnaireDTO questionnaireDTO, User author) {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setName(questionnaireDTO.getName());
        questionnaire.setDescription(questionnaireDTO.getDescription());

        if (questionnaireDTO.getFields() != null) {
            List<Field> fields = questionnaireDTO.getFields().stream()
                    .map(fieldDto -> fieldService.getFieldById(fieldDto.getId()))
                    .collect(Collectors.toList());
            questionnaire.setFields(fields);
        }else{
            throw new RuntimeException("Invalid questionnaire");
        }


        questionnaire.setAuthor(author);
        questionnaireRepository.save(questionnaire);
        return convertToDTO(questionnaire);
    }



    public QuestionnaireDTO update(Long id, QuestionnaireDTO questionnaireDTO, User author) {
        Questionnaire existingQuestionnaire = questionnaireRepository.findByIdAndAuthor(id, author);
        if (existingQuestionnaire == null) {
            throw new RuntimeException("Questionnaire not found or you don't have permission to edit it");
        }

        existingQuestionnaire.setName(questionnaireDTO.getName());
        existingQuestionnaire.setDescription(questionnaireDTO.getDescription());

        if (questionnaireDTO.getFields() != null) {
            // Clear existing fields
            existingQuestionnaire.getFields().clear();

            // Add new fields
            List<Field> fields = questionnaireDTO.getFields().stream()
                    .map(fieldService::convertToEntity)
                    .collect(Collectors.toList());
            existingQuestionnaire.setFields(fields);
        }

        Questionnaire updatedQuestionnaire = questionnaireRepository.save(existingQuestionnaire);
        return convertToDTO(updatedQuestionnaire);
    }

    public void delete(Long id, User author) {
        Questionnaire questionnaire = questionnaireRepository.findByIdAndAuthor(id, author);
        if (questionnaire == null) {
            throw new RuntimeException("Questionnaire not found or you don't have permission to delete it");
        }
        questionnaireRepository.delete(questionnaire);
    }

    public List<QuestionnaireDTO> getAllByAuthor(User author) {
        return questionnaireRepository.findByAuthor(author).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public QuestionnaireDTO getByIdAndAuthor(Long id, User author) {
        Questionnaire questionnaire = questionnaireRepository.findByIdAndAuthor(id, author);
        if (questionnaire == null) {
            throw new RuntimeException("Questionnaire not found or you don't have permission to view it");
        }
        return convertToDTO(questionnaire);
    }

    @Transactional
    public QuestionnaireDTO getById(Long id) {
        Optional<Questionnaire> questionnaire = questionnaireRepository.findById(id);
        if (questionnaire.isEmpty()) {
            throw new RuntimeException("Questionnaire not found or you don't have permission to view it");
        }
        return convertToDTO(questionnaire.orElse(null));
    }


    private QuestionnaireDTO convertToDTO(Questionnaire questionnaire) {
        QuestionnaireDTO dto = new QuestionnaireDTO();
        dto.setId(questionnaire.getId());
        dto.setName(questionnaire.getName());
        dto.setDescription(questionnaire.getDescription());

        if (questionnaire.getFields() != null) {
            List<FieldDTO> fieldDTOs = questionnaire.getFields().stream()
                    .map(fieldService::convertToDTO)
                    .collect(Collectors.toList());
            dto.setFields(fieldDTOs);
        }

        return dto;
    }
}