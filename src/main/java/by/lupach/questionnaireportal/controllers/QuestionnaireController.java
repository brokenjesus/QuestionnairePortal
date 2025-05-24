package by.lupach.questionnaireportal.controllers;

import by.lupach.questionnaireportal.dtos.QuestionnaireDTO;
import by.lupach.questionnaireportal.dtos.ResponseDTO;
import by.lupach.questionnaireportal.models.Response;
import by.lupach.questionnaireportal.models.User;
import by.lupach.questionnaireportal.services.QuestionnaireService;
import by.lupach.questionnaireportal.services.ResponseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questionnaires")
public class QuestionnaireController {
    private final QuestionnaireService questionnaireService;
    private final ResponseService responseService;

    public QuestionnaireController(QuestionnaireService questionnaireService, ResponseService responseService) {
        this.questionnaireService = questionnaireService;
        this.responseService = responseService;
    }

    @PostMapping
    public ResponseEntity<QuestionnaireDTO> create(
            @RequestBody QuestionnaireDTO questionnaireDTO,
            @AuthenticationPrincipal User author) {

        QuestionnaireDTO created = questionnaireService.create(questionnaireDTO, author);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionnaireDTO> update(
            @PathVariable Long id,
            @RequestBody QuestionnaireDTO questionnaireDTO,
            @AuthenticationPrincipal User author) {
        QuestionnaireDTO updated = questionnaireService.update(id, questionnaireDTO, author);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{id}/response")
    public ResponseEntity<Response> response(
            @PathVariable Long id,
            @RequestBody ResponseDTO responseDTO) {
        Response savedResponse = responseService.saveResponse(responseDTO);
        return ResponseEntity.ok(savedResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User author) {
        questionnaireService.delete(id, author);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<QuestionnaireDTO>> getAllByAuthor(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @AuthenticationPrincipal User author) {

        if (page != null && size != null) {
            List<QuestionnaireDTO> questionnaires = questionnaireService.getAllByAuthor(author, page, size);
            return ResponseEntity.ok(questionnaires);
        } else {
            List<QuestionnaireDTO> questionnaires = questionnaireService.getAllByAuthor(author);
            return ResponseEntity.ok(questionnaires);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionnaireDTO> getById(
            @PathVariable Long id) {
        QuestionnaireDTO questionnaire = questionnaireService.getById(id);
        return ResponseEntity.ok(questionnaire);
    }
}