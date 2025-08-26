package coursePro.mr.taxiApp.service;

import coursePro.mr.taxiApp.dto.ConducteurDto;
import coursePro.mr.taxiApp.dto.WalletDto;
import coursePro.mr.taxiApp.entity.Wallet;

public interface WalletService {
    
    WalletDto getOrCreateWallet(ConducteurDto conducteur);
    public WalletDto getWalletWithTransactions(Long conducteurId);
    public WalletDto getWallet(Long conducteurId);
    void ajouterSolde(WalletDto wallet, double montant);
    void retirerCommission(WalletDto walletDto, double montant);
    boolean soldeSuffisant(WalletDto wallet, double montantMinimum);
    void bloquerSiSoldeInsuffisant(WalletDto wallet, double minimum);
}
