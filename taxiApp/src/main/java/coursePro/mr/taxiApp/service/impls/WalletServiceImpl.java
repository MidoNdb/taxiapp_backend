package coursePro.mr.taxiApp.service.impls;

import org.springframework.stereotype.Service;

import coursePro.mr.taxiApp.dao.WalletRepository;
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

    public WalletServiceImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

@Override
public WalletDto getOrCreateWallet(ConducteurDto conducteurDto) {
    System.out.println("🔍 Récupération ou création wallet pour conducteur ID: " + conducteurDto.getId());
    
    // Création d'une instance partielle de Conducteur avec uniquement l'ID
    Conducteur conducteur = new Conducteur();
    conducteur.setId(conducteurDto.getId());

    // Recherche ou création du portefeuille
    Wallet wallet = walletRepository.findByConducteur(conducteur)
            .orElseGet(() -> {
                System.out.println("📝 Création nouveau wallet pour conducteur ID: " + conducteurDto.getId());
                Wallet newWallet = new Wallet();
                newWallet.setConducteur(conducteur);
                newWallet.setSolde(0.0);
                newWallet.setActif(false);
                return walletRepository.save(newWallet);
            });

    System.out.println("✅ Wallet récupéré/créé - ID: " + wallet.getId());

    // Conversion en DTO SANS les transactions pour de meilleures performances
    return WalletMapper.toDtoWithoutTransactions(wallet);
}@Override
public WalletDto getWalletWithTransactions(Long conducteurId) {
    System.out.println("🔍 Recherche/création wallet pour conducteur ID: " + conducteurId);
    
    // Créer un ConducteurDto minimal
    ConducteurDto conducteurDto = new ConducteurDto();
    conducteurDto.setId(conducteurId);
    
    // Utiliser getOrCreateWallet pour s'assurer que le wallet existe
    // Cette méthode gère déjà la logique de création si nécessaire
    WalletDto walletDto = getOrCreateWallet(conducteurDto);
    
    // Maintenant récupérer le wallet avec ses transactions
    Wallet wallet = walletRepository.findByConducteurId(conducteurId)
            .orElseThrow(() -> new RuntimeException("Erreur inattendue : wallet devrait exister"));
    
    System.out.println("✅ Wallet final - ID: " + wallet.getId() + 
                       ", Transactions: " + (wallet.getTransactions() != null ? wallet.getTransactions().size() : 0));
    
    // Conversion en DTO avec toutes les transactions
    return WalletMapper.toDto(wallet);
}
//     @Override
// public WalletDto getWalletWithTransactions(Long conducteurId) {
//     System.out.println("🔍 Recherche wallet pour conducteur ID: " + conducteurId);
    
//     // Création d'une instance partielle de Conducteur avec uniquement l'ID
//     Conducteur conducteur = new Conducteur();
//     conducteur.setId(conducteurId);

//     // Recherche du wallet existant
//     Wallet wallet = walletRepository.findByConducteur(conducteur)
//             .orElseThrow(() -> {
//                 System.err.println("❌ Wallet non trouvé pour le conducteur ID: " + conducteurId);
//                 return new RuntimeException("Wallet non trouvé pour ce conducteur");
//             });

//     System.out.println("✅ Wallet trouvé - ID: " + wallet.getId() + 
//                       ", Transactions: " + (wallet.getTransactions() != null ? wallet.getTransactions().size() : 0));

//     // Conversion en DTO avec toutes les transactions
//     return WalletMapper.toDto(wallet);
// }

   // Cette méthode existe déjà dans votre WalletServiceImpl, mais voici une version améliorée


@Override
@Transactional
public void retirerCommission(WalletDto walletDto, double montant) {
    try {
        // Récupérer le wallet depuis la base de données
        Wallet wallet = walletRepository.findById(walletDto.getId())
            .orElseThrow(() -> new RuntimeException("Wallet introuvable avec l'ID : " + walletDto.getId()));

        // Vérifier que le solde est suffisant
        if (wallet.getSolde() < montant) {
            // Désactiver le wallet si solde insuffisant
            wallet.setActif(false);
            walletRepository.save(wallet);

            throw new RuntimeException(
                String.format("Solde insuffisant. Disponible: %.2f MRU, Requis: %.2f MRU",
                    wallet.getSolde(), montant)
            );
        }

        // Déduire le montant
        double nouveauSolde = wallet.getSolde() - montant;
        wallet.setSolde(nouveauSolde);

        // Vérifier si le nouveau solde justifie une désactivation
        final double SOLDE_MINIMUM = 50.0;
        if (nouveauSolde < SOLDE_MINIMUM) {
            wallet.setActif(false);
        }

        // Sauvegarder
        walletRepository.save(wallet);

    } catch (Exception e) {
        throw new RuntimeException("Erreur lors du retrait de commission: " + e.getMessage());
    }
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
