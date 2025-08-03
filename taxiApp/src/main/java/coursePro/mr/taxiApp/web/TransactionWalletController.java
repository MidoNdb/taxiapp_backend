package coursePro.mr.taxiApp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import coursePro.mr.taxiApp.dto.ConducteurDto;
import coursePro.mr.taxiApp.dto.TransactionWalletDto;
import coursePro.mr.taxiApp.dto.WalletDto;
import coursePro.mr.taxiApp.entity.Conducteur;
import coursePro.mr.taxiApp.security.JwtService;
import coursePro.mr.taxiApp.service.TransactionWalletService;
import coursePro.mr.taxiApp.service.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.io.File;
import java.io.IOException;
import java.util.List;
@RestController
@RequestMapping("/wallet")
public class TransactionWalletController {

    private final WalletService walletService;
    private final TransactionWalletService transactionWalletService;

    @Value("${app.upload.dir:uploads/preuves}")
    private String uploadDir;

    @Autowired
    private JwtService jwtService;

    public TransactionWalletController(WalletService walletService, TransactionWalletService transactionWalletService) {
        this.walletService = walletService;
        this.transactionWalletService = transactionWalletService;
    }

    @GetMapping("/conducteur/transactions")
    public ResponseEntity<List<TransactionWalletDto>> getMesTransactions(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).build();
            }

            String token = authHeader.substring(7);
            Long id = jwtService.extractUserId(token);
            
            ConducteurDto conducteur = new ConducteurDto(id);
            WalletDto wallet = walletService.getOrCreateWallet(conducteur);
            List<TransactionWalletDto> transactions = transactionWalletService.getTransactionsParWallet(wallet);
            
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            System.err.println("❌ Erreur getMesTransactions: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/conducteur/rechargement")
    public ResponseEntity<?> rechargerWallet(
            @RequestParam("montant") double montant,
            @RequestParam("image") MultipartFile image,
            HttpServletRequest request
    ) {
        System.out.println("🚀 === DEBUT RECHARGEMENT ===");
        System.out.println("🔍 Montant reçu: " + montant);
        System.out.println("🔍 Image: " + (image != null ? image.getOriginalFilename() : "null"));
        System.out.println("🔍 Taille image: " + (image != null ? image.getSize() : "0"));

        try {
            // ✅ Validation des paramètres
            if (montant <= 0) {
                return ResponseEntity.badRequest().body("Montant invalide");
            }
            
            if (image.isEmpty()) {
                return ResponseEntity.badRequest().body("Image requise");
            }

            // ✅ Validation de l'authentification
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("Token manquant");
            }

            String token = authHeader.substring(7);
            Long id = jwtService.extractUserId(token);
            ConducteurDto conducteur = new ConducteurDto(id);
            
            WalletDto walletDto = walletService.getOrCreateWallet(conducteur);

            // ✅ CORRECTION: Utiliser un chemin simple et sûr
            String tempDir = System.getProperty("java.io.tmpdir");
            String fullUploadPath = tempDir + File.separator + "taxiapp-uploads";
            File uploadDirectory = new File(fullUploadPath);
            
            System.out.println("📁 Utilisation du répertoire temporaire: " + fullUploadPath);
            
            if (!uploadDirectory.exists()) {
                boolean created = uploadDirectory.mkdirs();
                System.out.println("📁 Dossier créé: " + created + " - Chemin: " + uploadDirectory.getAbsolutePath());
                
                // ✅ Vérification des permissions
                if (!uploadDirectory.canWrite()) {
                    System.err.println("❌ Pas de permissions d'écriture sur: " + uploadDirectory.getAbsolutePath());
                    return ResponseEntity.status(500).body("Permissions insuffisantes pour créer le dossier");
                }
            }

            // ✅ Générer un nom de fichier unique et sécurisé
            String originalFilename = image.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            
            String filename = System.currentTimeMillis() + "_conducteur_" + id + fileExtension;
            
            // ✅ SOLUTION: Utiliser transferTo avec un File simple
            File destinationFile = new File(uploadDirectory, filename);
            
            System.out.println("💾 Sauvegarde vers: " + destinationFile.getAbsolutePath());

            try {
                // ✅ Méthode plus simple et fiable
                image.transferTo(destinationFile);
                System.out.println("✅ Fichier sauvegardé avec succès");
            } catch (IOException e) {
                System.err.println("❌ Erreur lors de la sauvegarde: " + e.getMessage());
                e.printStackTrace();
                
                // ✅ Tentative alternative avec Files.copy
                try {
                    Files.copy(image.getInputStream(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("✅ Fichier sauvegardé avec méthode alternative");
                } catch (IOException fallbackException) {
                    System.err.println("❌ Échec de la méthode alternative: " + fallbackException.getMessage());
                    return ResponseEntity.status(500).body("Impossible de sauvegarder le fichier");
                }
            }

            // ✅ Vérifier que le fichier a bien été créé
            if (!destinationFile.exists() || destinationFile.length() == 0) {
                return ResponseEntity.status(500).body("Le fichier n'a pas été sauvegardé correctement");
            }

            System.out.println("✅ Fichier vérifié - Taille: " + destinationFile.length() + " bytes");

            // URL relative pour la base de données
            String preuveUrl = "/uploads/preuves/" + filename;

            // Enregistrement de la transaction
            transactionWalletService.enregistrerRechargement(walletDto, montant, preuveUrl);
            
            System.out.println("✅ Rechargement enregistré avec succès");
            return ResponseEntity.ok().body("Rechargement en attente de validation");
            
        } catch (Exception e) {
            System.err.println("❌ ERREUR COMPLETE: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur interne: " + e.getMessage());
        } finally {
            System.out.println("🏁 === FIN RECHARGEMENT ===");
        }
    }
}