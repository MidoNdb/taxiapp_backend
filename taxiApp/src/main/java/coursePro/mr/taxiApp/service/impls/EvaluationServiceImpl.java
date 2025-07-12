package coursePro.mr.taxiApp.service.impls;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coursePro.mr.taxiApp.dao.EvaluationRepository;
import coursePro.mr.taxiApp.entity.Evaluation;
import coursePro.mr.taxiApp.service.EvaluationService;

@Service
public class EvaluationServiceImpl implements EvaluationService {

    @Autowired
    private EvaluationRepository repo;

    @Override
    public Evaluation findById(Long id) {
        return repo.findById(id).orElseThrow();
    }

    @Override
    public Evaluation findByCourseId(Long courseId) {
        return repo.findByCourse_Id(courseId).orElseThrow();
    }

    @Override
    public List<Evaluation> findByAuteurId(Long auteurId) {
        return repo.findByAuteur_Id(auteurId);
    }

    @Override
    public List<Evaluation> findByCibleId(Long cibleId) {
        return repo.findByCible_Id(cibleId);
    }

    @Override
    public Evaluation save(Evaluation evaluation) {
        return repo.save(evaluation);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }
}

