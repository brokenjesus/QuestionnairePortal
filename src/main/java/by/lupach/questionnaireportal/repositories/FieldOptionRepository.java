package by.lupach.questionnaireportal.repositories;

import by.lupach.questionnaireportal.models.FieldOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldOptionRepository extends JpaRepository<FieldOption, Long> {
    List<FieldOption> findByFieldId(Long fieldId);

    void deleteByFieldId(Long fieldId);
}