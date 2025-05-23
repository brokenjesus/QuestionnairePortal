package by.lupach.questionnaireportal.repositories;

import by.lupach.questionnaireportal.models.ResponseFieldAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResponseAnswerRepository extends JpaRepository<ResponseFieldAnswer, Long> {
}
