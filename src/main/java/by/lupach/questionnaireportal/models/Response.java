package by.lupach.questionnaireportal.models;

import lombok.*;

import jakarta.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "responses")
public class Response {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "respondent_id", nullable = false)
    private User respondent;

    @ManyToOne
    @JoinColumn(name = "questionnaire_id", nullable = false)
    private Questionnaire questionnaire;

    @OneToMany(mappedBy = "response", cascade = CascadeType.ALL)
    private List<ResponseFieldAnswer> answers;
}