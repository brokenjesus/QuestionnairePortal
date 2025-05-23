package by.lupach.questionnaireportal.repositories;

import by.lupach.questionnaireportal.models.Response;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResponseRepository extends JpaRepository<Response, Long> {
}
