package by.lupach.questionnaireportal.services;

import by.lupach.questionnaireportal.dtos.FieldDTO;
import by.lupach.questionnaireportal.dtos.PageResponseDTO;
import by.lupach.questionnaireportal.dtos.QuestionnaireDTO;
import by.lupach.questionnaireportal.models.*;
import by.lupach.questionnaireportal.repositories.QuestionnaireRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class QuestionnaireService {
    private final QuestionnaireRepository questionnaireRepository;
    private final FieldService fieldService;

    public QuestionnaireService(QuestionnaireRepository questionnaireRepository,
                                FieldService fieldService) {
        this.questionnaireRepository = questionnaireRepository;
        this.fieldService = fieldService;
    }

    public QuestionnaireDTO create(QuestionnaireDTO questionnaireDTO, User author) {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setName(questionnaireDTO.getName());
        questionnaire.setDescription(questionnaireDTO.getDescription());
        questionnaire.setAuthor(author);

        if (questionnaireDTO.getFields() == null || questionnaireDTO.getFields().isEmpty()) {
            throw new RuntimeException("Invalid questionnaire: fields are required");
        }

        List<QuestionnaireField> questionnaireFields = new ArrayList<>();

        for (int i = 0; i < questionnaireDTO.getFields().size(); i++) {
            FieldDTO fieldDto = questionnaireDTO.getFields().get(i);
            Field field = fieldService.getFieldById(fieldDto.getId());

            QuestionnaireField qf = new QuestionnaireField();
            qf.setQuestionnaire(questionnaire);
            qf.setField(field);
            qf.setFieldOrder(i);
            qf.setId(new QuestionnaireFieldId());

            questionnaireFields.add(qf);
        }

        questionnaire.setQuestionnaireFields(questionnaireFields);
        questionnaireRepository.save(questionnaire);
        return convertToDTO(questionnaire);
    }

    @Transactional
    public QuestionnaireDTO update(Long id, QuestionnaireDTO questionnaireDTO, User author) {
        Questionnaire existingQuestionnaire = questionnaireRepository.findByIdAndAuthor(id, author);
        if (existingQuestionnaire == null) {
            throw new RuntimeException("Questionnaire not found or you don't have permission to edit it");
        }

        existingQuestionnaire.setName(questionnaireDTO.getName());
        existingQuestionnaire.setDescription(questionnaireDTO.getDescription());

        if (questionnaireDTO.getFields() != null) {
            Map<Long, QuestionnaireField> existingFieldsMap = existingQuestionnaire.getQuestionnaireFields().stream()
                    .collect(Collectors.toMap(qf -> qf.getField().getId(), Function.identity()));

            List<QuestionnaireField> updatedFields = new ArrayList<>();

            for (int i = 0; i < questionnaireDTO.getFields().size(); i++) {
                FieldDTO fieldDTO = questionnaireDTO.getFields().get(i);
                Field field = fieldService.getFieldById(fieldDTO.getId());

                QuestionnaireField qf = existingFieldsMap.get(field.getId());
                if (qf == null) {
                    qf = new QuestionnaireField();
                    qf.setId(new QuestionnaireFieldId(existingQuestionnaire.getId(), field.getId()));
                    qf.setQuestionnaire(existingQuestionnaire);
                    qf.setField(field);
                }
                qf.setFieldOrder(i);
                updatedFields.add(qf);
            }

            existingQuestionnaire.getQuestionnaireFields().clear();
            existingQuestionnaire.getQuestionnaireFields().addAll(updatedFields);
        }

        Questionnaire updated = questionnaireRepository.save(existingQuestionnaire);
        return convertToDTO(updated);
    }


    public void delete(Long id, User author) {
        Questionnaire questionnaire = questionnaireRepository.findByIdAndAuthor(id, author);
        if (questionnaire == null) {
            throw new RuntimeException("Questionnaire not found or you don't have permission to delete it");
        }
        questionnaireRepository.delete(questionnaire);
    }

    public PageResponseDTO<QuestionnaireDTO> getAllByAuthor(User author, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Questionnaire> questionnairePage = questionnaireRepository.findByAuthor(author, pageable);

        List<QuestionnaireDTO> questionnaireDTOs = questionnairePage.stream()
                .map(this::convertToDTO)
                .toList();

        return new PageResponseDTO<>(
                questionnaireDTOs,
                questionnairePage.getNumber(),
                questionnairePage.getSize(),
                questionnairePage.getTotalElements(),
                questionnairePage.getTotalPages(),
                questionnairePage.isLast()
        );
    }

    public PageResponseDTO<QuestionnaireDTO> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Questionnaire> questionnairePage = questionnaireRepository.findAll(pageable);

        List<QuestionnaireDTO> questionnaireDTOs = questionnairePage.stream()
                .map(this::convertToDTO)
                .toList();

        return new PageResponseDTO<>(
                questionnaireDTOs,
                questionnairePage.getNumber(),
                questionnairePage.getSize(),
                questionnairePage.getTotalElements(),
                questionnairePage.getTotalPages(),
                questionnairePage.isLast()
        );
    }

    public List<QuestionnaireDTO> getAllByAuthor(User author) {
        return questionnaireRepository.findByAuthor(author).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    @Transactional
    public QuestionnaireDTO getById(Long id) {
        Optional<Questionnaire> questionnaire = questionnaireRepository.findById(id);
        if (questionnaire.isEmpty()) {
            throw new RuntimeException("Questionnaire not found");
        }
        return convertToDTO(questionnaire.get());
    }

    private QuestionnaireDTO convertToDTO(Questionnaire questionnaire) {
        QuestionnaireDTO dto = new QuestionnaireDTO();
        dto.setId(questionnaire.getId());
        dto.setName(questionnaire.getName());
        dto.setDescription(questionnaire.getDescription());

        if (questionnaire.getQuestionnaireFields() != null) {
            List<FieldDTO> fieldDTOs = questionnaire.getQuestionnaireFields().stream()
                    .sorted(Comparator.comparingInt(QuestionnaireField::getFieldOrder))
                    .map(qf -> fieldService.convertToDTO(qf.getField()))
                    .collect(Collectors.toList());
            dto.setFields(fieldDTOs);
        }
        return dto;
    }
}
