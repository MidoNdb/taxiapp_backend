package coursePro.mr.taxiApp.service.impls;

import org.springframework.stereotype.Service;

import coursePro.mr.taxiApp.dao.WalletRepository;
import coursePro.mr.taxiApp.dao.ConducteurRepository;
import coursePro.mr.taxiApp.dto.ConducteurDto;
import coursePro.mr.taxiApp.dto.WalletDto;
import coursePro.mr.taxiApp.entity.Conducteur;
import coursePro.mr.taxiApp.entity.Wallet;
import coursePro.mr.taxiApp.mapper.WalletMapper;
import coursePro.mr.taxiApp.service.WalletService;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final ConducteurRepository conducteurRepository;

    public WalletServiceImpl(WalletRepository walletRepository, ConducteurRepository conducteurRepository) {
        this.walletRepository = walletRepository;
        this.conducteurRepository = conducteurRepository;
    }

    @Override
    public WalletDto getOrCreateWallet(ConducteurDto conducteurDto) {
        // Récupérer le conducteur complet depuis la base de données
        Conducteur conducteur = conducteurRepository.findById(conducteurDto.getId())
                .orElseThrow(() -> new RuntimeException("Conducteur introuvable avec l'ID : " + conducteurDto.getId()));

        // Recherche ou création du portefeuille
        Wallet wallet = walletRepository.findByConducteur(conducteur)
                .orElseGet(() -> {
                    Wallet newWallet = new Wallet();
                    newWallet.setConducteur(conducteur);
                    newWallet.setSolde(0.0);
                    newWallet.setActif(false);
                    return walletRepository.save(newWallet);
                });

        return WalletMapper.toDtoWithoutTransactions(wallet);
    }

    @Override
    public WalletDto getWalletWithTransactions(Long conducteurId) {
        // Vérifier que le conducteur existe
        Conducteur conducteur = conducteurRepository.findById(conducteurId)
                .orElseThrow(() -> new RuntimeException("Conducteur introuvable avec l'ID : " + conducteurId));

        // Rechercher ou créer le wallet
        Wallet wallet = walletRepository.findByConducteur(conducteur)
                .orElseGet(() -> {
                    Wallet newWallet = new Wallet();
                    newWallet.setConducteur(conducteur);
                    newWallet.setSolde(0.0);
                    newWallet.setActif(false);
                    return walletRepository.save(newWallet);
                });

        return WalletMapper.toDto(wallet);
    }

    @Override
    @Transactional
    public void retirerCommission(WalletDto walletDto, double montant) {
        Wallet wallet = walletRepository.findById(walletDto.getId())
                .orElseThrow(() -> new RuntimeException("Wallet introuvable avec l'ID : " + walletDto.getId()));

        if (wallet.getSolde() < montant) {
            wallet.setActif(false);
            walletRepository.save(wallet);
            throw new RuntimeException(
                    String.format("Solde insuffisant. Disponible: %.2f MRU, Requis: %.2f MRU",
                            wallet.getSolde(), montant)
            );
        }

        double nouveauSolde = wallet.getSolde() - montant;
        wallet.setSolde(nouveauSolde);

        final double SOLDE_MINIMUM = 50.0;
        if (nouveauSolde < SOLDE_MINIMUM) {
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
        if (walletDto.getSolde() < minimum) {
            Wallet wallet = walletRepository.findById(walletDto.getId())
                    .orElseThrow(() -> new RuntimeException("Wallet introuvable"));
            wallet.setActif(false);
            walletRepository.save(wallet);
        }
    }

    @Override
    @Transactional
    public void ajouterSolde(WalletDto walletDto, double montant) {
        Wallet wallet = walletRepository.findById(walletDto.getId())
                .orElseThrow(() -> new RuntimeException("Wallet introuvable avec l'ID : " + walletDto.getId()));

        wallet.setSolde(wallet.getSolde() + montant);
        wallet.setDernierRechargement(LocalDateTime.now());
        wallet.setActif(true);
        walletRepository.save(wallet);
    }
}