package by.lupach.questionnaireportal.controllers;

import by.lupach.questionnaireportal.dtos.QuestionnaireDTO;
import by.lupach.questionnaireportal.models.User;
import by.lupach.questionnaireportal.services.QuestionnaireService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questionnaires")
public class QuestionnaireController {
    private final QuestionnaireService questionnaireService;

    public QuestionnaireController(QuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User author) {
        questionnaireService.delete(id, author);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<QuestionnaireDTO>> getAllByAuthor(
            @AuthenticationPrincipal User author) {
        List<QuestionnaireDTO> questionnaires = questionnaireService.getAllByAuthor(author);
        return ResponseEntity.ok(questionnaires);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionnaireDTO> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal User author) {
        QuestionnaireDTO questionnaire = questionnaireService.getById(id, author);
        return ResponseEntity.ok(questionnaire);
    }
}