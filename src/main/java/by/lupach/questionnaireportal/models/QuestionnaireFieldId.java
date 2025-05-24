package by.lupach.questionnaireportal.models;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class QuestionnaireFieldId implements Serializable {
    private Long questionnaireId;
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

