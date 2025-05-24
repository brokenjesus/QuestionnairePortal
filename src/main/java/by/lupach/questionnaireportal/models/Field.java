package by.lupach.questionnaireportal.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "fields")
@Schema(description = "Field represents a form field in a questionnaire")
public class Field {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the field", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "Author of the field")
    private User author;

    @Column(nullable = false)
    @Schema(description = "Label or question text of the field", example = "What is your name?", required = true)
    private String label;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Type of the field", example = "SINGLE_LINE_TEXT", required = true)
    private FieldType type;

    @Column(name = "is_active", nullable = false)
    @Schema(description = "Flag indicating if the field is active", example = "true", required = true)
    private boolean isActive;

    @Column(name = "is_required", nullable = false)
    @Schema(description = "Flag indicating if the field is required", example = "true", required = true)
    private boolean isRequired;

    @OneToMany(mappedBy = "field", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "List of available options for the field (for select, radio, checkbox types)")
    private List<FieldOption> options = new ArrayList<>();

    @OneToMany(mappedBy = "field", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionnaireField> questionnaireFields;

}
