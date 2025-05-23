package by.lupach.questionnaireportal.services;

import by.lupach.questionnaireportal.dtos.FieldDTO;
import by.lupach.questionnaireportal.exceptions.NotFoundException;
import by.lupach.questionnaireportal.models.Field;
import by.lupach.questionnaireportal.models.FieldOption;
import by.lupach.questionnaireportal.models.User;
import by.lupach.questionnaireportal.repositories.FieldOptionRepository;
import by.lupach.questionnaireportal.repositories.FieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FieldService {

    @Autowired
    private FieldRepository fieldRepository;
    @Autowired
    private FieldOptionRepository fieldOptionRepository;
    @Autowired
    private AuthenticationService authenticationService;

    public List<FieldDTO> getAllFields(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return fieldRepository.findAll(pageable).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<FieldDTO> getAllFieldsByAuthor(int page, int size, User author) {
        Pageable pageable = PageRequest.of(page, size);
        return fieldRepository.findByAuthor(author, pageable).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<FieldDTO> getActiveFields() {
        List<FieldDTO> activeFields = fieldRepository.findByIsActiveTrue()
                .stream()
                .map(this::convertToDTO)
                .toList();

        return activeFields;
    }

    public List<FieldDTO> getActiveFieldsByAuthor(User author) {
        List<FieldDTO> activeFields = fieldRepository.findByIsActiveTrueAndAuthor(author)
                .stream()
                .map(this::convertToDTO)
                .toList();

        return activeFields;
    }



    public FieldDTO getFieldDTOById(Long id) {
        Field field = fieldRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Field not found with id: " + id));
        return convertToDTO(field);
    }

    public Field getFieldById(Long id) {
        return fieldRepository.getById(id);
    }

    @Transactional
    public void createField(FieldDTO fieldDTO) {
        // 1. Создаем и сохраняем Field без опций
        Field field = Field.builder()
                .author(authenticationService.getCurrentUser())
                .label(fieldDTO.getLabel())
                .type(fieldDTO.getType())
                .isRequired(fieldDTO.isRequired())
                .isActive(fieldDTO.isActive())
                .build();

        Field savedField = fieldRepository.save(field);

        if (fieldDTO.getOptions() != null && !fieldDTO.getOptions().isEmpty()) {
            List<FieldOption> options = fieldDTO.getOptions().stream()
                    .map(optionValue -> {
                        FieldOption option = new FieldOption();
                        option.setValue(optionValue);
                        option.setField(savedField);
                        return option;
                    })
                    .collect(Collectors.toList());

            fieldOptionRepository.saveAll(options);

            savedField.setOptions(options);
        }
    }


    @Transactional
    public void updateField(Long id, FieldDTO fieldDTO) {
        Field field = fieldRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Field not found with id: " + id));

        field.setLabel(fieldDTO.getLabel());
        field.setType(fieldDTO.getType());
        field.setRequired(fieldDTO.isRequired());
        field.setActive(fieldDTO.isActive());

        field.getOptions().clear();
        if (fieldDTO.getOptions() != null && !fieldDTO.getOptions().isEmpty()) {
            List<FieldOption> newOptions = createOptionsForField(fieldDTO.getOptions(), field);
            field.getOptions().addAll(newOptions);
        }
    }


    @Transactional
    public void deleteField(Long id) {
        Field field = fieldRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Field not found with id: " + id));
        fieldOptionRepository.deleteByFieldId(id);
        fieldRepository.delete(field);
    }

    private List<FieldOption> createOptionsForField(List<String> optionValues, Field field) {
        return optionValues.stream()
                .map(value -> {
                    FieldOption option = new FieldOption();
                    option.setValue(value);
                    option.setField(field);
                    return option;
                })
                .collect(Collectors.toList());
    }

    public FieldDTO convertToDTO(Field field) {
        FieldDTO dto = new FieldDTO();
        dto.setId(field.getId());
        dto.setLabel(field.getLabel());
        dto.setType(field.getType());
        dto.setRequired(field.isRequired());
        dto.setActive(field.isActive());

        if (field.getOptions() != null) {
            dto.setOptions(field.getOptions().stream()
                    .map(FieldOption::getValue)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public Field convertToEntity(FieldDTO fieldDTO) {
        Field field = new Field();
        field.setId(fieldDTO.getId());
        field.setLabel(fieldDTO.getLabel());
        field.setType(fieldDTO.getType());
        field.setRequired(fieldDTO.isRequired());
        field.setActive(fieldDTO.isActive());

        // Convert options if present
        if (fieldDTO.getOptions() != null && !fieldDTO.getOptions().isEmpty()) {
            List<FieldOption> options = fieldDTO.getOptions().stream()
                    .map(optionValue -> {
                        FieldOption option = new FieldOption();
                        option.setValue(optionValue);
                        option.setField(field);  // Set back reference
                        return option;
                    })
                    .collect(Collectors.toList());
            field.setOptions(options);
        }

        return field;
    }

}