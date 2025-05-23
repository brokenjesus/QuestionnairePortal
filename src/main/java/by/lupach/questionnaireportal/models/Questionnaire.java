package by.lupach.questionnaireportal.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "questionnaires")
public class Questionnaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "questionnaire_fields",
            joinColumns = @JoinColumn(name = "questionnaire_id"),
            inverseJoinColumns = @JoinColumn(name = "field_id")
    )
    private List<Field> fields;

    @OneToMany(mappedBy = "questionnaire", cascade = CascadeType.ALL)
    private List<Response> responses;
}