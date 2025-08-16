package  coursePro.mr.taxiApp.dao;


import org.springframework.data.jpa.repository.JpaRepository;

import coursePro.mr.taxiApp.entity.TransactionWallet;
import coursePro.mr.taxiApp.entity.Wallet;

import java.util.List;

public interface TransactionWalletRepository extends JpaRepository<TransactionWallet, Long> {
    List<TransactionWallet> findByWallet(Wallet wallet);
    List<TransactionWallet> findAllByOrderByDateDesc();

}
