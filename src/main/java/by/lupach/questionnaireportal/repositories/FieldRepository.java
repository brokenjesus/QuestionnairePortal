package by.lupach.questionnaireportal.repositories;

import by.lupach.questionnaireportal.models.Field;
import by.lupach.questionnaireportal.models.Questionnaire;
import by.lupach.questionnaireportal.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FieldRepository extends JpaRepository<Field, Long> {
//    List<Field> findAll();
    List<Field> findByIsActiveTrue();
    List<Field>  findByIsActiveTrueAndAuthor(User author);
    Page<Field> findByAuthor(User author, Pageable pageable);
    Field getById(Long id);
}
