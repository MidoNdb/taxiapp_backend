package coursePro.mr.taxiApp.service.impls;

import coursePro.mr.taxiApp.dao.TransactionWalletRepository;
import coursePro.mr.taxiApp.dao.WalletRepository;
import coursePro.mr.taxiApp.dto.TransactionWalletDto;
import coursePro.mr.taxiApp.dto.WalletDto;
import coursePro.mr.taxiApp.entity.TransactionWallet;
import coursePro.mr.taxiApp.entity.Wallet;
import coursePro.mr.taxiApp.enums.StatutTransaction;
import coursePro.mr.taxiApp.enums.TypeTransaction;
import coursePro.mr.taxiApp.mapper.TransactionWalletMapper;
import coursePro.mr.taxiApp.mapper.WalletMapper;
import coursePro.mr.taxiApp.service.TransactionWalletService;
import coursePro.mr.taxiApp.service.WalletService;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import coursePro.mr.taxiApp.web.NotificationController;
import coursePro.mr.taxiApp.web.NotificationSocketController;

@Service
public class TransactionWalletServiceImpl implements TransactionWalletService {

    private final TransactionWalletRepository transactionRepo;
    private final WalletRepository walletRepository;
    private final WalletService walletService; 
    private final NotificationController notificationController;

    public TransactionWalletServiceImpl(
        TransactionWalletRepository transactionRepo,
        WalletRepository walletRepository,
        WalletService walletService,
        NotificationController notificationController
    ) {
        this.transactionRepo = transactionRepo;
        this.walletRepository = walletRepository;
        this.walletService = walletService;
        this.notificationController=notificationController;
    }

    @Override
    public TransactionWalletDto enregistrerRechargement(WalletDto walletDto, double montant, String preuveUrl) {
        Optional<Wallet> walletOpt = walletRepository.findById(walletDto.getId());
        if (walletOpt.isEmpty()) {
            throw new IllegalArgumentException("Wallet introuvable avec l'ID : " + walletDto.getId());
        }

        Wallet wallet = walletOpt.get();
        
        TransactionWallet transaction = new TransactionWallet();
        transaction.setWallet(wallet);
        transaction.setMontant(montant);
        transaction.setType(TypeTransaction.RECHARGEMENT);
        transaction.setPreuveUrl(preuveUrl);
        transaction.setStatut(StatutTransaction.EN_ATTENTE);

        TransactionWallet saved = transactionRepo.save(transaction);
        
        // 🚨 NOTIFICATION ADMIN - NOUVEAU
        notificationController.notifyAdminRechargement(saved);
        
        return TransactionWalletMapper.toDto(saved);
    }

  
    @Override
public List<TransactionWalletDto> getAll() {
    return transactionRepo.findAllByOrderByDateDesc().stream()
            .map(TransactionWalletMapper::toDto)
            .collect(Collectors.toList());
}

   @Override
@Transactional
public void validerRechargement(TransactionWalletDto transactionDto) {
    TransactionWallet transaction = transactionRepo.findById(transactionDto.getId())
            .orElseThrow(() -> new IllegalArgumentException("Transaction introuvable"));

    if (transaction.getStatut() != StatutTransaction.EN_ATTENTE) {
        throw new IllegalStateException("Cette transaction a déjà été traitée.");
    }

    transaction.setStatut(StatutTransaction.VALIDEE);

    Wallet wallet = transaction.getWallet();
    wallet.setSolde(wallet.getSolde() + transaction.getMontant());
    wallet.setDernierRechargement(LocalDateTime.now());
    wallet.setActif(true);

    // ✅ Pas besoin de save séparé, Hibernate gère la persistance avec @Transactional
}

@Override
@Transactional
public void rejeterRechargement(TransactionWalletDto transactionDto) {
    TransactionWallet transaction = transactionRepo.findById(transactionDto.getId())
            .orElseThrow(() -> new IllegalArgumentException("Transaction introuvable"));

    if (transaction.getStatut() != StatutTransaction.EN_ATTENTE) {
        throw new IllegalStateException("Cette transaction a déjà été traitée.");
    }

    transaction.setStatut(StatutTransaction.REJETEE);
    // ✅ Pas besoin de save séparé ici non plus
}


    @Override
    public List<TransactionWalletDto> getTransactionsParWallet(WalletDto walletDto) {
        Optional<Wallet> walletOpt = walletRepository.findById(walletDto.getId());
        if (walletOpt.isEmpty()) {
            throw new IllegalArgumentException("Wallet introuvable");
        }

        List<TransactionWallet> transactions = transactionRepo.findByWallet(walletOpt.get());

        return transactions.stream()
                .map(TransactionWalletMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
 * ✅ Enregistre une transaction de commission avec validation du conducteur
 */

@Override
@Transactional
public TransactionWalletDto enregistrerCommission(WalletDto walletDto, double montant, 
                                                 Long courseId, Long conducteurId) {
    try {
        // Récupérer le wallet entity
        Wallet wallet = walletRepository.findById(walletDto.getId())
            .orElseThrow(() -> new RuntimeException("Wallet introuvable"));

        // Créer la transaction de commission
        TransactionWallet transaction = new TransactionWallet();
        transaction.setWallet(wallet);
        transaction.setMontant(montant);
        transaction.setType(TypeTransaction.COMMISSION);
        transaction.setStatut(StatutTransaction.VALIDEE);
        transaction.setDate(LocalDateTime.now());
        
        // Ajouter une description pour identifier la course
       // transaction.setDescription("Commission course #" + courseId);

        // Sauvegarder la transaction
        TransactionWallet savedTransaction = transactionRepo.save(transaction);

        return TransactionWalletMapper.toDto(savedTransaction);

    } catch (Exception e) {
        throw new RuntimeException("Erreur lors de l'enregistrement de la commission: " + e.getMessage());
    }
}




}
