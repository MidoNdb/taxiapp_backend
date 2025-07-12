package coursePro.mr.taxiApp.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import coursePro.mr.taxiApp.entity.Passager;

@Repository
public interface PassagerRepository extends JpaRepository<Passager, Long> {
    Optional<Passager> findByUtilisateur_Id(Long utilisateurId);
    
}

