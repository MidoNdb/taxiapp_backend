package coursePro.mr.taxiApp.service.impls;

import org.springframework.stereotype.Service;

import coursePro.mr.taxiApp.dao.WalletRepository;
import coursePro.mr.taxiApp.dto.ConducteurDto;
import coursePro.mr.taxiApp.dto.WalletDto;
import coursePro.mr.taxiApp.entity.Conducteur;
import coursePro.mr.taxiApp.entity.Wallet;
import coursePro.mr.taxiApp.mapper.WalletMapper;
import coursePro.mr.taxiApp.service.WalletService;

import java.time.LocalDateTime;
@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    public WalletServiceImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
public WalletDto getOrCreateWallet(ConducteurDto conducteurDto) {
    // Création d'une instance partielle de Conducteur avec uniquement l'ID
    Conducteur conducteur = new Conducteur();
    conducteur.setId(conducteurDto.getId());

    // Recherche ou création du portefeuille
    Wallet wallet = walletRepository.findByConducteur(conducteur)
            .orElseGet(() -> {
                Wallet newWallet = new Wallet();
                newWallet.setConducteur(conducteur);
                newWallet.setSolde(0.0);
                newWallet.setActif(false);
                return walletRepository.save(newWallet);
            });

    // Conversion en DTO et retour
    return WalletMapper.toDto(wallet);
}


    @Override
    public void retirerCommission(WalletDto walletDto, double montant) {
        Wallet wallet = WalletMapper.toEntity(walletDto);
        wallet.setSolde(wallet.getSolde() - montant);
        if (wallet.getSolde() < montant) {
            wallet.setActif(false);
        }
        walletRepository.save(wallet);
    }

    @Override
    public boolean soldeSuffisant(WalletDto walletDto, double montantMinimum) {
        return walletDto.getSolde() >= montantMinimum;
    }

    @Override
    public void bloquerSiSoldeInsuffisant(WalletDto walletDto, double minimum) {
        Wallet wallet = WalletMapper.toEntity(walletDto);
        if (wallet.getSolde() < minimum) {
            wallet.setActif(false);
            walletRepository.save(wallet);
        }
    }
    @Override
public void ajouterSolde(WalletDto walletDto, double montant) {
    Wallet wallet = walletRepository.findById(walletDto.getId())
        .orElseThrow(() -> new RuntimeException("Wallet introuvable avec l'ID : " + walletDto.getId()));

    wallet.setSolde(wallet.getSolde() + montant);
    wallet.setDernierRechargement(LocalDateTime.now());
    wallet.setActif(true);
    walletRepository.save(wallet);
}

}
