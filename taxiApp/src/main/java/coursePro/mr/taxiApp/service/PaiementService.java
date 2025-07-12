package coursePro.mr.taxiApp.service;

import coursePro.mr.taxiApp.entity.Paiement;

public interface PaiementService {
    Paiement findById(Long id);
    Paiement findByCourseId(Long courseId);
    Paiement save(Paiement paiement);
    void delete(Long id);
}

