package coursePro.mr.taxiApp.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import coursePro.mr.taxiApp.entity.Evaluation;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    List<Evaluation> findByAuteur_Id(Long auteurId);
    List<Evaluation> findByCible_Id(Long cibleId);
    Optional<Evaluation> findByCourse_Id(Long courseId);
}

