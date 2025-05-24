package by.lupach.questionnaireportal.repositories;

import by.lupach.questionnaireportal.models.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResponseRepository extends JpaRepository<Response, Long> {

    @Query("SELECT r FROM Response r JOIN FETCH r.answers WHERE r.questionnaire.id = :questionnaireId")
    List<Response> findByQuestionnaireIdWithAnswers(@Param("questionnaireId") Long questionnaireId);

    @Query(value = "SELECT DISTINCT r FROM Response r LEFT JOIN FETCH r.answers WHERE r.questionnaire.id = :questionnaireId",
            countQuery = "SELECT COUNT(r) FROM Response r WHERE r.questionnaire.id = :questionnaireId")
    Page<Response> findByQuestionnaireIdWithAnswers(@Param("questionnaireId") Long questionnaireId, Pageable pageable);
}
