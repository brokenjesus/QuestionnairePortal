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
@Table(name = "questionnaires")
@Schema(description = "Questionnaire entity represents a survey or form created by a user")
public class Questionnaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the questionnaire", example = "5", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Name/title of the questionnaire", example = "Customer Feedback", required = true)
    private String name;

    @Column(nullable = false)
    @Schema(description = "Description of the questionnaire", example = "Survey to gather customer opinions", required = true)
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "Author of the questionnaire")
    private User author;

    @OneToMany(mappedBy = "questionnaire", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionnaireField> questionnaireFields;

    @OneToMany(mappedBy = "questionnaire", cascade = CascadeType.ALL)
    private List<Response> respons;
}
