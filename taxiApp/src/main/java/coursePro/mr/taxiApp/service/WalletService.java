package coursePro.mr.taxiApp.service;

import coursePro.mr.taxiApp.dto.ConducteurDto;
import coursePro.mr.taxiApp.dto.WalletDto;

public interface WalletService {
    
    WalletDto getOrCreateWallet(ConducteurDto conducteur);
    void ajouterSolde(WalletDto wallet, double montant);
    void retirerCommission(WalletDto wallet, double montant);
    boolean soldeSuffisant(WalletDto wallet, double montantMinimum);
    void bloquerSiSoldeInsuffisant(WalletDto wallet, double minimum);
}
