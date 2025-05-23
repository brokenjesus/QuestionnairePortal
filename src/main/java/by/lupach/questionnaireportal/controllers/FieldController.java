package by.lupach.questionnaireportal.controllers;


import by.lupach.questionnaireportal.dtos.FieldDTO;
import by.lupach.questionnaireportal.models.User;
import by.lupach.questionnaireportal.services.AuthenticationService;
import by.lupach.questionnaireportal.services.FieldService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fields")
@CrossOrigin(origins = "*")
public class FieldController {

    @Autowired
    private FieldService fieldService;
    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping
    public ResponseEntity<List<FieldDTO>> getAllFields(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal User author
    ) {
        return ResponseEntity.ok(fieldService.getAllFieldsByAuthor(page, size, author));
//        return ResponseEntity.ok(fieldService.getAllFields(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FieldDTO> getFieldById(@PathVariable Long id) {
        return ResponseEntity.ok(fieldService.getFieldDTOById(id));
    }

    @GetMapping("/active")
    public ResponseEntity<List<FieldDTO>> getActiveFields(@AuthenticationPrincipal User author) {
        return ResponseEntity.ok(fieldService.getActiveFieldsByAuthor(author));
    }

    @PostMapping
    public ResponseEntity createField(@RequestBody @Valid FieldDTO FieldDTO) {
        fieldService.createField(FieldDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity updateField(
            @PathVariable Long id,
            @RequestBody @Valid FieldDTO FieldDTO
    ) {
        fieldService.updateField(id, FieldDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteField(@PathVariable Long id) {
        fieldService.deleteField(id);
        return ResponseEntity.noContent().build();
    }
}