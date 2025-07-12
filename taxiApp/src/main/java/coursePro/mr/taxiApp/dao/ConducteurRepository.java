package coursePro.mr.taxiApp.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import coursePro.mr.taxiApp.entity.Conducteur;

@Repository
public interface ConducteurRepository extends JpaRepository<Conducteur, Long> {
    Optional<Conducteur> findByUtilisateur_Id(Long utilisateurId);
}

