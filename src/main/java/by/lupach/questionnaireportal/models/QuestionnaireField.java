package by.lupach.questionnaireportal.models;

import jakarta.persistence.*;
import lombok.*;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "questionnaire_fields")
@Schema(description = "Represents a mapping between a Questionnaire and a Field with an order")
public class QuestionnaireField {

    @EmbeddedId
    @Schema(description = "Composite primary key consisting of questionnaireId and fieldId")
    private QuestionnaireFieldId id = new QuestionnaireFieldId();

    @ManyToOne
    @MapsId("questionnaireId")
    @JoinColumn(name = "questionnaire_id")
    @Schema(description = "The questionnaire this field belongs to")
    private Questionnaire questionnaire;

    @ManyToOne
    @MapsId("fieldId")
    @JoinColumn(name = "field_id")
    @Schema(description = "The field assigned to the questionnaire")
    private Field field;

    @Column(name = "field_order")
    @Schema(description = "Order of the field within the questionnaire", example = "1")
    private int fieldOrder;
}
