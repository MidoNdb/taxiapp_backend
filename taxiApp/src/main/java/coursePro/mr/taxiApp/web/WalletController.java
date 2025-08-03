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
            System.out.println("🔑 Authorities: " + (auth != null ? auth.getAuthorities() : "null"));
            
            String authHeader = request.getHeader("Authorization");
            System.out.println("🔑 Auth Header: " + (authHeader != null ? "Bearer ***" : "null"));
            
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                System.err.println("❌ Token manquant ou invalide");
                return ResponseEntity.status(401).build();
            }

            String token = authHeader.substring(7);
            Long id = jwtService.extractUserId(token);
            String role = jwtService.extractRole(token); // Assurez-vous que cette méthode existe
            
            System.out.println("🆔 User ID: " + id);
            System.out.println("🔐 Role extraite du token: " + role);

            ConducteurDto conducteur = new ConducteurDto(id);
            WalletDto wallet = walletService.getOrCreateWallet(conducteur);

            System.out.println("✅ Wallet récupéré avec succès");
            return ResponseEntity.ok(wallet);

        } catch (Exception e) {
            System.err.println("❌ Erreur récupération wallet: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/conducteur/rechargement")
    public ResponseEntity<?> rechargerWallet(
            @RequestParam("montant") double montant,
            @RequestParam("imageFile") MultipartFile imageFile,
            HttpServletRequest request
    ) {
        System.out.println("🚀 === DEBUT RECHARGEMENT ===");
        System.out.println("🔍 Montant reçu: " + montant);
        System.out.println("🔍 Image: " + (imageFile != null ? imageFile.getOriginalFilename() : "null"));
        System.out.println("🔍 Taille image: " + (imageFile != null ? imageFile.getSize() : "0"));

        try {
            // ✅ Vérification de l'authentification et des rôles
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("🔑 Authentication principal: " + (auth != null ? auth.getName() : "null"));
            System.out.println("🔑 Authorities: " + (auth != null ? auth.getAuthorities() : "null"));
            
            if (auth == null || !auth.isAuthenticated()) {
                System.err.println("❌ Utilisateur non authentifié");
                return ResponseEntity.status(401).body("Utilisateur non authentifié");
            }
            
            // Vérification du rôle CONDUCTEUR
            boolean hasConducteurRole = auth.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_CONDUCTEUR"));
            
            if (!hasConducteurRole) {
                System.err.println("❌ Rôle CONDUCTEUR manquant. Rôles actuels: " + auth.getAuthorities());
                return ResponseEntity.status(403).body("Accès interdit - Rôle CONDUCTEUR requis");
            }
            
            System.out.println("✅ Rôle CONDUCTEUR vérifié");

            // ✅ Validation des paramètres
            if (montant <= 0) {
                return ResponseEntity.badRequest().body("Montant invalide");
            }
            
            if (imageFile == null || imageFile.isEmpty()) {
                return ResponseEntity.badRequest().body("Image requise");
            }

            // ✅ Extraction de l'ID utilisateur
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("Token manquant");
            }

            String token = authHeader.substring(7);
            Long id = jwtService.extractUserId(token);
            
            System.out.println("🆔 ID Conducteur: " + id);
            
            ConducteurDto conducteur = new ConducteurDto(id);
            WalletDto walletDto = walletService.getOrCreateWallet(conducteur);

            // ✅ Gestion des fichiers - Chemin simplifié
            String tempDir = System.getProperty("java.io.tmpdir");
            String fullUploadPath = tempDir + File.separator + "taxiapp-uploads";
            File uploadDirectory = new File(fullUploadPath);
            
            System.out.println("📁 Répertoire d'upload: " + fullUploadPath);
            
            if (!uploadDirectory.exists()) {
                boolean created = uploadDirectory.mkdirs();
                System.out.println("📁 Dossier créé: " + created);
                
                if (!uploadDirectory.canWrite()) {
                    System.err.println("❌ Permissions insuffisantes: " + uploadDirectory.getAbsolutePath());
                    return ResponseEntity.status(500).body("Permissions insuffisantes");
                }
            }

            // ✅ Nom de fichier sécurisé
            String originalFilename = imageFile.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            
            String filename = System.currentTimeMillis() + "_conducteur_" + id + fileExtension;
            File destinationFile = new File(uploadDirectory, filename);
            
            System.out.println("💾 Sauvegarde vers: " + destinationFile.getAbsolutePath());

            // ✅ Sauvegarde du fichier
            try {
                imageFile.transferTo(destinationFile);
                System.out.println("✅ Fichier sauvegardé avec succès");
            } catch (IOException e) {
                System.err.println("❌ Erreur sauvegarde: " + e.getMessage());
                
                try {
                    Files.copy(imageFile.getInputStream(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("✅ Fichier sauvegardé avec méthode alternative");
                } catch (IOException fallbackException) {
                    System.err.println("❌ Échec méthode alternative: " + fallbackException.getMessage());
                    return ResponseEntity.status(500).body("Impossible de sauvegarder le fichier");
                }
            }

            // ✅ Vérification du fichier
            if (!destinationFile.exists() || destinationFile.length() == 0) {
                return ResponseEntity.status(500).body("Fichier non sauvegardé correctement");
            }

            System.out.println("✅ Fichier vérifié - Taille: " + destinationFile.length() + " bytes");

            // ✅ URL pour la base de données
            String preuveUrl = "/uploads/preuves/" + filename;

            // ✅ Enregistrement de la transaction
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

    // ✅ Valider une transaction
    @PutMapping("/{id}/valider")
    public ResponseEntity<?> validerTransaction(@PathVariable Long id) {
        try {
            TransactionWalletDto dto = new TransactionWalletDto();
            dto.setId(id);
            transactionService.validerRechargement(dto);
            return ResponseEntity.ok("Transaction validée avec succès.");
        } catch (Exception e) {
            System.err.println("❌ Erreur validation: " + e.getMessage());
            return ResponseEntity.status(500).body("Erreur lors de la validation");
        }
    }

    // ❌ Rejeter une transaction
    @PutMapping("/{id}/rejeter")
    public ResponseEntity<?> rejeterTransaction(@PathVariable Long id) {
        try {
            TransactionWalletDto dto = new TransactionWalletDto();
            dto.setId(id);
            transactionService.rejeterRechargement(dto);
            return ResponseEntity.ok("Transaction rejetée.");
        } catch (Exception e) {
            System.err.println("❌ Erreur rejet: " + e.getMessage());
            return ResponseEntity.status(500).body("Erreur lors du rejet");
        }
    }
}