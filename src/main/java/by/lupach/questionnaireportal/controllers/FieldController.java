package by.lupach.questionnaireportal.controllers;

import by.lupach.questionnaireportal.dtos.FieldDTO;
import by.lupach.questionnaireportal.models.User;
import by.lupach.questionnaireportal.services.FieldService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fields")
@CrossOrigin(origins = "*")
@Tag(name = "Fields", description = "Endpoints for managing form fields")
public class FieldController {

    private final FieldService fieldService;

    public FieldController(FieldService fieldService) {
        this.fieldService = fieldService;
    }

    @Operation(summary = "Get all fields by author", description = "Get paginated list of fields created by the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fields retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<?> getAllFieldsByAuthor(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(hidden = true)
            @AuthenticationPrincipal User author) {
        return ResponseEntity.ok(fieldService.getAllFieldsByAuthor(page, size, author));
    }

    @Operation(summary = "Get field by ID", description = "Get a specific field by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Field retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Field not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getFieldById(
            @Parameter(description = "ID of the field to retrieve", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(fieldService.getFieldDTOById(id));
    }

    @Operation(summary = "Get active fields", description = "Get all active fields created by the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active fields retrieved successfully")
    })
    @GetMapping("/active")
    public ResponseEntity<?> getActiveFields(
            @Parameter(hidden = true)
            @AuthenticationPrincipal User author) {
        return ResponseEntity.ok(fieldService.getActiveFieldsByAuthor(author));
    }

    @Operation(summary = "Create new field", description = "Create a new form field")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Field created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid field data")
    })
    @PostMapping
    public ResponseEntity<?> createField(@RequestBody @Valid FieldDTO FieldDTO) {
        fieldService.createField(FieldDTO);
        return ResponseEntity.created(null).body("Field created successfully");
    }

    @Operation(summary = "Update field", description = "Update an existing field")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Field updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid field data"),
            @ApiResponse(responseCode = "404", description = "Field not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateField(
            @Parameter(description = "ID of the field to update", required = true)
            @PathVariable Long id,

            @RequestBody @Valid FieldDTO FieldDTO) {
        fieldService.updateField(id, FieldDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete field", description = "Delete a field by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Field deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Field not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteField(
            @Parameter(description = "ID of the field to delete", required = true)
            @PathVariable Long id) {
        fieldService.deleteField(id);
        return ResponseEntity.noContent().build();
    }
}