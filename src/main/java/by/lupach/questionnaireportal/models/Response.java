package by.lupach.questionnaireportal.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "responses")
@Schema(description = "Response submitted for a questionnaire")
public class Response {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the response", example = "100", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "questionnaire_id", nullable = false)
    @Schema(description = "Questionnaire for which this response is submitted")
    private Questionnaire questionnaire;

    @OneToMany(mappedBy = "response", cascade = CascadeType.ALL)
    @Schema(description = "List of answers submitted for fields in the questionnaire")
    private List<ResponseFieldAnswer> answers;
}
