package by.lupach.questionnaireportal.repositories;

import by.lupach.questionnaireportal.models.Questionnaire;
import org.springframework.data.jpa.repository.JpaRepository;

import by.lupach.questionnaireportal.models.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionnaireRepository extends JpaRepository<Questionnaire, Long> {
    List<Questionnaire> findByAuthor(User author);
    Questionnaire findByIdAndAuthor(Long id, User author);
}