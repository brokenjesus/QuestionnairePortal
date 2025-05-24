package by.lupach.questionnaireportal.models;

import lombok.*;

import jakarta.persistence.*;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "response_answers")
@Schema(description = "An answer to a specific field within a response")
public class ResponseFieldAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the answer", example = "500", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Textual answer provided by the respondent", example = "Yes, I agree", required = true)
    private String answer;

    @ManyToOne
    @JoinColumn(name = "field_id", nullable = false)
    @Schema(description = "Field to which this answer corresponds")
    private Field field;

    @ManyToOne
    @JoinColumn(name = "response_id", nullable = false)
    @Schema(description = "Response that contains this answer")
    private Response response;
}
