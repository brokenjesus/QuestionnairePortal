package by.lupach.questionnaireportal.controllers;

import by.lupach.questionnaireportal.dtos.PageResponseDTO;
import by.lupach.questionnaireportal.dtos.QuestionnaireDTO;
import by.lupach.questionnaireportal.dtos.ResponseDTO;
import by.lupach.questionnaireportal.models.Response;
import by.lupach.questionnaireportal.models.User;
import by.lupach.questionnaireportal.services.QuestionnaireService;
import by.lupach.questionnaireportal.services.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questionnaires")
@Tag(name = "Questionnaires", description = "Endpoints for managing questionnaires")
public class QuestionnaireController {
    private final QuestionnaireService questionnaireService;
    private final ResponseService responseService;

    public QuestionnaireController(QuestionnaireService questionnaireService, ResponseService responseService) {
        this.questionnaireService = questionnaireService;
        this.responseService = responseService;
    }

    @Operation(summary = "Create questionnaire", description = "Create a new questionnaire")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Questionnaire created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid questionnaire data")
    })
    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody QuestionnaireDTO questionnaireDTO,
            @Parameter(hidden = true) @AuthenticationPrincipal User author) {
        questionnaireService.create(questionnaireDTO, author);
        return ResponseEntity.created(null).body("Questionnaire created successfully");
    }

    @Operation(summary = "Update questionnaire", description = "Update an existing questionnaire")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Questionnaire updated successfully",
                    content = @Content(schema = @Schema(implementation = QuestionnaireDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid questionnaire data"),
            @ApiResponse(responseCode = "404", description = "Questionnaire not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @Parameter(description = "ID of the questionnaire to update", required = true)
            @PathVariable Long id,

            @RequestBody QuestionnaireDTO questionnaireDTO,

            @Parameter(hidden = true)
            @AuthenticationPrincipal User author) {
        QuestionnaireDTO updated = questionnaireService.update(id, questionnaireDTO, author);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Submit response", description = "Submit a response to a questionnaire")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Response submitted successfully",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Invalid response data"),
            @ApiResponse(responseCode = "404", description = "Questionnaire not found")
    })
    @PostMapping("/{id}/response")
    public ResponseEntity<?> response(
            @Parameter(description = "ID of the questionnaire to respond to", required = true)
            @PathVariable Long id,

            @RequestBody ResponseDTO responseDTO) {
        Response savedResponse = responseService.saveResponse(responseDTO);
        return ResponseEntity.ok(savedResponse);
    }

    @Operation(summary = "Delete questionnaire", description = "Delete a questionnaire by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Questionnaire deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Questionnaire not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @Parameter(description = "ID of the questionnaire to delete", required = true)
            @PathVariable Long id,

            @Parameter(hidden = true)
            @AuthenticationPrincipal User author) {
        questionnaireService.delete(id, author);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get questionnaires by author",
            description = "Get questionnaires created by the authenticated user, optionally paginated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Questionnaires retrieved successfully")
    })
    @GetMapping("/my")
    public ResponseEntity<?> getAllByAuthor(
            @Parameter(description = "Page number (optional)", required = false)
            @RequestParam(required = false) Integer page,

            @Parameter(description = "Page size (optional)", required = false)
            @RequestParam(required = false) Integer size,

            @Parameter(hidden = true)
            @AuthenticationPrincipal User author) {
        if (page != null && size != null) {
            PageResponseDTO<QuestionnaireDTO> questionnaires = questionnaireService.getAllByAuthor(author, page, size);
            return ResponseEntity.ok(questionnaires);
        } else {
            List<QuestionnaireDTO> questionnaires = questionnaireService.getAllByAuthor(author);
            return ResponseEntity.ok(questionnaires);
        }
    }

    @Operation(summary = "Get all questionnaires",
            description = "Get all questionnaires, paginated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Questionnaires retrieved successfully")
    })
    @GetMapping("/all")
    public ResponseEntity<?> getAll(
            @Parameter(description = "Page number", required = true)
            @RequestParam(required = false) Integer page,

            @Parameter(description = "Page size", required = true)
            @RequestParam(required = false) Integer size) {
        PageResponseDTO<QuestionnaireDTO> questionnaires = questionnaireService.getAll(page, size);
        return ResponseEntity.ok(questionnaires);
    }

    @Operation(summary = "Get questionnaire by ID", description = "Get a specific questionnaire by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Questionnaire retrieved successfully",
                    content = @Content(schema = @Schema(implementation = QuestionnaireDTO.class))),
            @ApiResponse(responseCode = "404", description = "Questionnaire not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @Parameter(description = "ID of the questionnaire to retrieve", required = true)
            @PathVariable Long id) {
        QuestionnaireDTO questionnaire = questionnaireService.getById(id);
        return ResponseEntity.ok(questionnaire);
    }
}