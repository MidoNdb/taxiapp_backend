package coursePro.mr.taxiApp.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import coursePro.mr.taxiApp.dto.TransactionWalletDto;
import coursePro.mr.taxiApp.dto.WalletDto;
import coursePro.mr.taxiApp.security.JwtService;
import coursePro.mr.taxiApp.service.TransactionWalletService;
import coursePro.mr.taxiApp.service.WalletService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    private final TransactionWalletService transactionService;
    private final JwtService jwtService;
    private final WalletService walletService;
    private final TransactionWalletService transactionWalletService;
    @Value("${app.upload.dir:uploads/preuves}")
    private String uploadDir;

    public WalletController(TransactionWalletService transactionService, JwtService jwtService,
                            WalletService walletService, TransactionWalletService transactionWalletService) {
        this.transactionService = transactionService;
        this.jwtService = jwtService;
        this.walletService = walletService;
        this.transactionWalletService = transactionWalletService;
    }
@GetMapping("/conducteur/mon_wallet")
public ResponseEntity<WalletDto> getMonWallet(HttpServletRequest request) {
    try {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }

        String token = authHeader.substring(7);
        Long conducteurId = jwtService.extractUserId(token);
        
        WalletDto wallet = walletService.getWalletWithTransactions(conducteurId);
        
        return ResponseEntity.ok(wallet);
        
    } catch (RuntimeException e) {
        return ResponseEntity.status(404)
                .body(null);
    } catch (Exception e) {
        return ResponseEntity.status(500).build();
    }
}
    
@PutMapping("/admin/validate/{id}")
public ResponseEntity<?> validerTransactionAdmin(@PathVariable Long id) {
    try {
        System.out.println("🔍 Validation transaction ID: " + id);
        
        TransactionWalletDto dto = new TransactionWalletDto();
        dto.setId(id);
        transactionService.validerRechargement(dto);
        
        System.out.println("✅ Transaction validée avec succès");
        return ResponseEntity.ok("Transaction validée avec succès.");
    } catch (Exception e) {
        System.err.println("❌ Erreur validation: " + e.getMessage());
        return ResponseEntity.status(500).body("Erreur lors de la validation: " + e.getMessage());
    }
}

// ❌ Rejeter une transaction - ADMIN  
@PutMapping("/admin/reject/{id}")
public ResponseEntity<?> rejeterTransactionAdmin(@PathVariable Long id) {
    try {
        System.out.println("🔍 Rejet transaction ID: " + id);
        
        TransactionWalletDto dto = new TransactionWalletDto();
        dto.setId(id);
        transactionService.rejeterRechargement(dto);
        
        System.out.println("✅ Transaction rejetée avec succès");
        return ResponseEntity.ok("Transaction rejetée avec succès.");
    } catch (Exception e) {
        System.err.println("❌ Erreur rejet: " + e.getMessage());
        return ResponseEntity.status(500).body("Erreur lors du rejet: " + e.getMessage());
    }
}

@GetMapping("/conducteur/walletconducteur")
    public ResponseEntity<?> getWalletConducteur(HttpServletRequest request) {
        try {
            
            // Extraire l'ID du conducteur depuis le token JWT
            String token = request.getHeader("Authorization").substring(7);
            Long conducteurId = jwtService.extractUserId(token);
            
            // Récupérer le wallet
            WalletDto wallet = walletService.getWallet(conducteurId);
            
            return ResponseEntity.ok(wallet);
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Wallet non trouvé",
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "Erreur serveur",
                "message", e.getMessage()
            ));
        }
    }
    
    
    @GetMapping("/conducteur/can-accept-course")
    public ResponseEntity<?> canAcceptCourse(
            @RequestParam double montantCourse,
            HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization").substring(7);
            Long conducteurId = jwtService.extractUserId(token);
            
            WalletDto wallet = walletService.getWallet(conducteurId);
            
            // Vérifier si le wallet est actif
            if (!wallet.getActif()) {
                return ResponseEntity.ok(Map.of(
                    "canAccept", false,
                    "reason", "Wallet inactif"
                ));
            }
            
            // Calculer la commission (7%)
            double commission = montantCourse * 0.07;
            
            // Vérifier le solde
            boolean canAccept = wallet.getSolde() >= commission;
            
            return ResponseEntity.ok(Map.of(
                "canAccept", canAccept,
                "soldeActuel", wallet.getSolde(),
                "commissionRequise", commission,
                "reason", canAccept ? "OK" : "Solde insuffisant"
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "Erreur lors de la vérification",
                "message", e.getMessage()
            ));
        }
    }
}