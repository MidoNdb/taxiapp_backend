package coursePro.mr.taxiApp.service;
import java.util.List;

import coursePro.mr.taxiApp.dto.TransactionWalletDto;
import coursePro.mr.taxiApp.dto.WalletDto;

public interface TransactionWalletService {
    TransactionWalletDto enregistrerRechargement(WalletDto wallet, double montant, String preuveUrl);
    void validerRechargement(TransactionWalletDto transaction);
    void rejeterRechargement(TransactionWalletDto transaction);
    public List<TransactionWalletDto> getAll();
    List<TransactionWalletDto> getTransactionsParWallet(WalletDto wallet);
    TransactionWalletDto enregistrerCommission(WalletDto walletDto, double montantCommission, Long courseId, Long conducteurId);
}
 