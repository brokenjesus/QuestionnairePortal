package by.lupach.questionnaireportal.models;

import jakarta.persistence.*;
import lombok.*;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "field_options")
@Schema(description = "FieldOption represents a selectable option for a Field")
public class FieldOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the option", example = "10", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Value of the option", example = "Yes", required = true)
    private String value;

    @ManyToOne
    @JoinColumn(name = "field_id", nullable = false)
    @Schema(description = "Field to which this option belongs")
    private Field field;
}
