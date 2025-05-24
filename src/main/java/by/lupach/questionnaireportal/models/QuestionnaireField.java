package by.lupach.questionnaireportal.models;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "questionnaire_fields")
public class QuestionnaireField {

    @EmbeddedId
    private QuestionnaireFieldId id = new QuestionnaireFieldId();

    @ManyToOne
    @MapsId("questionnaireId")
    @JoinColumn(name = "questionnaire_id")
    private Questionnaire questionnaire;

    @ManyToOne
    @MapsId("fieldId")
    @JoinColumn(name = "field_id")
    private Field field;

    @Column(name = "field_order")
    private int fieldOrder;
}
