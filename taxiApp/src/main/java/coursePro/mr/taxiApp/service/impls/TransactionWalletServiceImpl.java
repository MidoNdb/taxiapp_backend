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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionWalletServiceImpl implements TransactionWalletService {

    private final TransactionWalletRepository transactionRepo;
    private final WalletRepository walletRepository;
    private final WalletService walletService;

    public TransactionWalletServiceImpl(
        TransactionWalletRepository transactionRepo,
        WalletRepository walletRepository,
        WalletService walletService
    ) {
        this.transactionRepo = transactionRepo;
        this.walletRepository = walletRepository;
        this.walletService = walletService;
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
        return TransactionWalletMapper.toDto(saved);
    }
    @Override
public List<TransactionWalletDto> getAll() {
    return transactionRepo.findAll().stream()
            .map(TransactionWalletMapper::toDto)
            .collect(Collectors.toList());
}

    @Override
    @Transactional
    public void validerRechargement(TransactionWalletDto transactionDto) {
        TransactionWallet transaction = transactionRepo.findById(transactionDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Transaction introuvable"));
        transaction.setStatut(StatutTransaction.VALIDEE);
        transactionRepo.save(transaction);
        walletService.ajouterSolde(WalletMapper.toDto(transaction.getWallet()), transaction.getMontant());
    }

    @Override
    public void rejeterRechargement(TransactionWalletDto transactionDto) {
        TransactionWallet transaction = transactionRepo.findById(transactionDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Transaction introuvable"));
        transaction.setStatut(StatutTransaction.REJETEE);
        transactionRepo.save(transaction);
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
}
