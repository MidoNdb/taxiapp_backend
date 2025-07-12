package coursePro.mr.taxiApp.service.impls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coursePro.mr.taxiApp.dao.PaiementRepository;
import coursePro.mr.taxiApp.entity.Paiement;
import coursePro.mr.taxiApp.service.PaiementService;

@Service
public class PaiementServiceImpl implements PaiementService {

    @Autowired
    private PaiementRepository repo;

    @Override
    public Paiement findById(Long id) {
        return repo.findById(id).orElseThrow();
    }

    @Override
    public Paiement findByCourseId(Long courseId) {
        return repo.findByCourse_Id(courseId).orElseThrow();
    }

    @Override
    public Paiement save(Paiement paiement) {
        return repo.save(paiement);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }
}

