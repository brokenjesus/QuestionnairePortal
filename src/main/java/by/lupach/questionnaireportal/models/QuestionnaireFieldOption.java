package by.lupach.questionnaireportal.models;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "questionnaire_field_options")
public class QuestionnaireFieldOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // К какому конкретному полю анкеты относится опция
    @ManyToOne
    @JoinColumn(name = "questionnaire_field_id", nullable = false)
    private QuestionnaireField questionnaireField;

    // Значение опции (можно хранить либо строку, либо ссылку на FieldOption)
    @ManyToOne
    @JoinColumn(name = "field_option_id")
    private FieldOption fieldOption;

    // Если нужно, можно добавить поле "customValue", если разрешено вводить собственный текст
    @Column
    private String customValue;
}
