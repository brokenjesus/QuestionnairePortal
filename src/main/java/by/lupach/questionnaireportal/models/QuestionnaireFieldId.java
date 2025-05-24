package by.lupach.questionnaireportal.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@Schema(description = "Composite key for QuestionnaireField entity")
public class QuestionnaireFieldId implements Serializable {

    @Schema(description = "ID of the questionnaire", example = "1")
    private Long questionnaireId;

    @Schema(description = "ID of the field", example = "10")
    private Long fieldId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuestionnaireFieldId that)) return false;
        return Objects.equals(questionnaireId, that.questionnaireId) &&
                Objects.equals(fieldId, that.fieldId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionnaireId, fieldId);
    }
}

