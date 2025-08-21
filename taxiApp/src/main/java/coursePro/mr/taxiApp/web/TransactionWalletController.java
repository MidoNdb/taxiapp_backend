package coursePro.mr.taxiApp.web;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import coursePro.mr.taxiApp.dto.ConducteurDto;
import coursePro.mr.taxiApp.dto.TransactionWalletDto;
import coursePro.mr.taxiApp.dto.WalletDto;
import coursePro.mr.taxiApp.security.JwtService;
import coursePro.mr.taxiApp.service.TransactionWalletService;
import coursePro.mr.taxiApp.service.WalletService;
import jakarta.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import coursePro.mr.taxiApp.service.FileUploadService;
@RestController
@RequestMapping("/transactions")
public class TransactionWalletController {

    private final WalletService walletService;
    private final TransactionWalletService transactionWalletService;
    private final  JwtService jwtService;
    private final FileUploadService fileUploadService;
   
    
    public TransactionWalletController(WalletService walletService,JwtService jwtService ,
    TransactionWalletService transactionWalletService,FileUploadService fileUploadService) {
        this.walletService = walletService;
        this.jwtService=jwtService;
        this.transactionWalletService = transactionWalletService;
        this.fileUploadService=fileUploadService;
    }


   @GetMapping("/admin/allTransactions")
    public ResponseEntity<List<TransactionWalletDto>> getAllTransactions(HttpServletRequest request) {
        try {
            List<TransactionWalletDto> transactions = transactionWalletService.getAll();
            
            // ✅ Construire les URLs complètes des images
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + 
                           request.getServerPort() + request.getContextPath();
            
            for (TransactionWalletDto transaction : transactions) {
                if (transaction.getPreuveUrl() != null && !transaction.getPreuveUrl().isEmpty()) {
                    // Si l'URL commence par "/uploads", ajouter le préfixe complet
                    if (transaction.getPreuveUrl().startsWith("/uploads")) {
                        transaction.setPreuveUrl(baseUrl + "/api" + transaction.getPreuveUrl());
                    }
                    // Si c'est déjà une URL complète, la laisser telle quelle
                    else if (!transaction.getPreuveUrl().startsWith("http")) {
                        transaction.setPreuveUrl(baseUrl + transaction.getPreuveUrl());
                    }
                }
            }
            
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

@GetMapping("/uploads/preuves/{filename}")
public ResponseEntity<Resource> getProofImage(
    @PathVariable String filename,
    @RequestParam(required = false) String token,
    HttpServletRequest request) {
    
    try {
        // ✅ Vérifier l'authentification
        String authHeader = request.getHeader("Authorization");
        String authToken = null;
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            authToken = authHeader.substring(7);
        } else if (token != null) {
            authToken = token; // Token passé en paramètre URL
        }
        
        if (authToken == null) {
            return ResponseEntity.status(401).build();
        }
        
        // Vérifier que c'est un admin
        try {
            Long userId = jwtService.extractUserId(authToken);
            // Vérifier le rôle admin ici si nécessaire
            System.out.println("🔑 Accès image autorisé pour userId: " + userId);
        } catch (Exception e) {
            System.err.println("❌ Token invalide: " + e.getMessage());
            return ResponseEntity.status(401).build();
        }
        
        // Votre code existant pour servir l'image...
        System.out.println("🖼️ Demande d'image de preuve: " + filename);
        
        String projectRoot = System.getProperty("user.dir");
        Path imagePath = Paths.get(projectRoot, "uploads", "preuves", filename);
        
        if (!Files.exists(imagePath)) {
            return ResponseEntity.notFound().build();
        }
        
        Resource resource = new FileSystemResource(imagePath.toFile());
        
        String contentType = "image/jpeg"; // Ou votre logique de détection
        
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .body(resource);
                
    } catch (Exception e) {
        System.err.println("❌ Erreur lors de la récupération de l'image: " + e.getMessage());
        return ResponseEntity.status(500).build();
    }
}


    
    
    // ✅ Récupérer un wallet spécifique avec toutes ses transactions
    @GetMapping("/admin/wallet/{conducteurId}")
    public ResponseEntity<WalletDto> getWalletWithTransactions(@PathVariable Long conducteurId) {
        return ResponseEntity.ok(walletService.getWalletWithTransactions(conducteurId));
    }
    
@PostMapping("/conducteur/rechargement")
public ResponseEntity<?> rechargerWallet(
        @RequestParam("montant") double montant,
        @RequestParam("imageFile") MultipartFile imageFile,
        HttpServletRequest request
) {
    try {
        
        if (montant <= 0) {
            System.out.println("❌ Montant invalide: " + montant);
            return ResponseEntity.badRequest().body(
                Map.of("error", "Montant invalide", "montant", montant)
            );
        }
        
        if (imageFile == null || imageFile.isEmpty()) {
            System.out.println("❌ Image manquante ou vide");
            return ResponseEntity.badRequest().body(
                Map.of("error", "Image requise")
            );
        }

        // Extraction de l'ID utilisateur
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("❌ Token manquant");
            return ResponseEntity.status(401).body(
                Map.of("error", "Token manquant")
            );
        }

        String token = authHeader.substring(7);
        Long conducteurId = jwtService.extractUserId(token);
        System.out.println("🟢 Conducteur ID: " + conducteurId);
        
        // Récupérer le wallet
        ConducteurDto conducteur = new ConducteurDto(conducteurId);
        WalletDto walletDto = walletService.getOrCreateWallet(conducteur);
        System.out.println("🟢 Wallet récupéré: " + walletDto.getId());

        // ✅ Sauvegarder l'image avec le service - AVEC DEBUGGING
        System.out.println("🟡 Tentative de sauvegarde de l'image...");
        String imageUrl;
        try {
            imageUrl = fileUploadService.saveImage(imageFile, conducteurId);
            System.out.println("🟢 Image sauvegardée: " + imageUrl);
        } catch (Exception e) {
            System.err.println("❌ ERREUR lors de la sauvegarde de l'image: " + e.getMessage());
            e.printStackTrace();
            // Retourner l'erreur spécifique de sauvegarde
            return ResponseEntity.status(500).body(
                Map.of("error", "Erreur lors de la sauvegarde de l'image: " + e.getMessage())
            );
        }
        
        // Enregistrement de la transaction
        System.out.println("🟡 Enregistrement de la transaction...");
        TransactionWalletDto transaction;
        try {
            transaction = transactionWalletService.enregistrerRechargement(
                walletDto, montant, imageUrl
            );
            System.out.println("🟢 Transaction enregistrée: " + transaction.getId());
        } catch (Exception e) {
            System.err.println("❌ ERREUR lors de l'enregistrement de la transaction: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(
                Map.of("error", "Erreur lors de l'enregistrement: " + e.getMessage())
            );
        }
        
        System.out.println("✅ Rechargement traité avec succès");
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Rechargement en attente de validation",
            "transaction", transaction,
            "imageUrl", imageUrl
        ));
        
    } catch (IllegalArgumentException e) {
        System.err.println("❌ Argument invalide: " + e.getMessage());
        return ResponseEntity.badRequest().body(
            Map.of("error", e.getMessage())
        );
    } catch (Exception e) {
        System.err.println("❌ Erreur générale rechargement: " + e.getMessage());
        e.printStackTrace();
        return ResponseEntity.status(500).body(
            Map.of("error", "Erreur interne du serveur: " + e.getMessage())
        );
    }
}

// ✅ Valider une transaction
@PutMapping("/admin/validate/{transactionId}")
public ResponseEntity<String> validateTransaction(@PathVariable Long transactionId) {
    try {
        // Créer un DTO avec l'ID pour passer au service
        TransactionWalletDto transactionDto = new TransactionWalletDto();
        transactionDto.setId(transactionId);
        
        // Appeler la méthode du service avec le bon nom
        transactionWalletService.validerRechargement(transactionDto);
        
        System.out.println("✅ Transaction " + transactionId + " validée avec succès");
        return ResponseEntity.ok("Transaction validée avec succès");
        
    } catch (IllegalArgumentException e) {
        System.err.println("❌ Transaction introuvable: " + transactionId);
        return ResponseEntity.badRequest().body("Transaction introuvable: " + e.getMessage());
    } catch (IllegalStateException e) {
        System.err.println("❌ Transaction déjà traitée: " + transactionId);
        return ResponseEntity.badRequest().body("Cette transaction a déjà été traitée: " + e.getMessage());
    } catch (Exception e) {
        System.err.println("❌ Erreur validation transaction " + transactionId + ": " + e.getMessage());
        e.printStackTrace();
        return ResponseEntity.status(500).body("Erreur lors de la validation: " + e.getMessage());
    }
}

// ✅ Rejeter une transaction
@PutMapping("/admin/reject/{transactionId}")
public ResponseEntity<String> rejectTransaction(@PathVariable Long transactionId) {
    try {
        // Créer un DTO avec l'ID pour passer au service
        TransactionWalletDto transactionDto = new TransactionWalletDto();
        transactionDto.setId(transactionId);
        
        // Appeler la méthode du service avec le bon nom
        transactionWalletService.rejeterRechargement(transactionDto);
        
        System.out.println("✅ Transaction " + transactionId + " rejetée avec succès");
        return ResponseEntity.ok("Transaction rejetée avec succès");
        
    } catch (IllegalArgumentException e) {
        System.err.println("❌ Transaction introuvable: " + transactionId);
        return ResponseEntity.badRequest().body("Transaction introuvable: " + e.getMessage());
    } catch (IllegalStateException e) {
        System.err.println("❌ Transaction déjà traitée: " + transactionId);
        return ResponseEntity.badRequest().body("Cette transaction a déjà été traitée: " + e.getMessage());
    } catch (Exception e) {
        System.err.println("❌ Erreur rejet transaction " + transactionId + ": " + e.getMessage());
        e.printStackTrace();
        return ResponseEntity.status(500).body("Erreur lors du rejet: " + e.getMessage());
    }
}
}