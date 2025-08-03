package coursePro.mr.taxiApp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import coursePro.mr.taxiApp.entity.Conducteur;
import coursePro.mr.taxiApp.entity.Wallet;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByConducteur(Conducteur conducteur);
}
