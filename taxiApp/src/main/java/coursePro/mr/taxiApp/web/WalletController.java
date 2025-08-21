package coursePro.mr.taxiApp.web;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import coursePro.mr.taxiApp.dto.ConducteurDto;
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
        System.out.println("🔍 === DEBUT GET WALLET ===");
        
        // Vérification de l'authentification via SecurityContext
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("🔑 Authentication: " + (auth != null ? auth.getName() : "null"));
        
        String authHeader = request.getHeader("Authorization");
        System.out.println("🔑 Auth Header: " + (authHeader != null ? "Bearer ***" : "null"));
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.err.println("❌ Token manquant ou invalide");
            return ResponseEntity.status(401).build();
        }

        String token = authHeader.substring(7);
        Long conducteurId = jwtService.extractUserId(token);
        String role = jwtService.extractRole(token);
        
        System.out.println("🆔 User ID: " + conducteurId);
        System.out.println("🔐 Role extraite du token: " + role);
        
        // Cette méthode va maintenant créer le wallet s'il n'existe pas
        WalletDto wallet = walletService.getWalletWithTransactions(conducteurId);
        
        System.out.println("✅ Wallet récupéré/créé avec succès - Transactions: " +
                           (wallet.getTransactions() != null ? wallet.getTransactions().size() : 0));
        return ResponseEntity.ok(wallet);
        
    } catch (Exception e) {
        System.err.println("❌ Erreur récupération/création wallet: " + e.getMessage());
        e.printStackTrace();
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
}