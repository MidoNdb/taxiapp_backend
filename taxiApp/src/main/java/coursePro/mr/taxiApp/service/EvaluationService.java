package coursePro.mr.taxiApp.service;

import java.util.List;

import coursePro.mr.taxiApp.entity.Evaluation;

public interface EvaluationService {
    Evaluation findById(Long id);
    Evaluation findByCourseId(Long courseId);
    List<Evaluation> findByAuteurId(Long auteurId);
    List<Evaluation> findByCibleId(Long cibleId);
    Evaluation save(Evaluation evaluation);
    void delete(Long id);
}

