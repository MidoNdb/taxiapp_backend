package coursePro.mr.taxiApp.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import coursePro.mr.taxiApp.dto.TransactionWalletDto;
import coursePro.mr.taxiApp.service.TransactionWalletService;

@RestController
@RequestMapping("/admin/transactions")
public class WalletController {

    private final TransactionWalletService transactionService;

    public WalletController(TransactionWalletService transactionService) {
        this.transactionService = transactionService;
    }

    // 🔍 Liste toutes les transactions (à paginer selon besoin)
    @GetMapping
    public ResponseEntity<List<TransactionWalletDto>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAll());
    }

    // ✅ Valider une transaction
    @PutMapping("/{id}/valider")
    public ResponseEntity<?> validerTransaction(@PathVariable Long id) {
        TransactionWalletDto dto = new TransactionWalletDto();
        dto.setId(id);
        transactionService.validerRechargement(dto);
        return ResponseEntity.ok("Transaction validée avec succès.");
    }

    // ❌ Rejeter une transaction
    @PutMapping("/{id}/rejeter")
    public ResponseEntity<?> rejeterTransaction(@PathVariable Long id) {
        TransactionWalletDto dto = new TransactionWalletDto();
        dto.setId(id);
        transactionService.rejeterRechargement(dto);
        return ResponseEntity.ok("Transaction rejetée.");
    }
}

