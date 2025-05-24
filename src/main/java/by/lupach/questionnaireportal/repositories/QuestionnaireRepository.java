package by.lupach.questionnaireportal.repositories;

import by.lupach.questionnaireportal.models.Questionnaire;
import by.lupach.questionnaireportal.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionnaireRepository extends JpaRepository<Questionnaire, Long> {
    Page<Questionnaire> findByAuthor(User author, Pageable pageable);

    List<Questionnaire> findByAuthor(User author);

    Questionnaire findByIdAndAuthor(Long id, User author);
}