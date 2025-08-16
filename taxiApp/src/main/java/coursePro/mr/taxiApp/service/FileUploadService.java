package coursePro.mr.taxiApp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileUploadService {
    
    @Value("${app.upload.images-dir}")
    private String uploadDir;
    
    public String saveImage(MultipartFile file, Long conducteurId) throws IOException {
        // Validation du fichier
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Fichier vide ou manquant");
        }
        
        // Validation du type de fichier
        // String contentType = file.getContentType();
        // if (contentType == null || !contentType.startsWith("image/")) {
        //     throw new IllegalArgumentException("Seules les images sont autorisées");
        // }
        
        // Créer le chemin complet
        String projectRoot = System.getProperty("user.dir");
        Path uploadPath = Paths.get(projectRoot, uploadDir);
        
        // Créer le dossier s'il n'existe pas
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Générer un nom de fichier unique
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        
        String uniqueFilename = System.currentTimeMillis() + "_" + 
                               UUID.randomUUID().toString().substring(0, 8) + 
                               "_conducteur_" + conducteurId + fileExtension;
        
        // Chemin complet du fichier
        Path filePath = uploadPath.resolve(uniqueFilename);
        
        // Sauvegarder le fichier
        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("✅ Fichier sauvegardé: " + filePath.toString());
        } catch (IOException e) {
            System.err.println("❌ Erreur sauvegarde: " + e.getMessage());
            throw new IOException("Impossible de sauvegarder le fichier", e);
        }
        
        // Retourner l'URL relative pour la base de données
        return "/transactions/uploads/preuves/" + uniqueFilename;
    }
    
    public boolean deleteImage(String imageUrl) {
        if (imageUrl == null || !imageUrl.startsWith("/transactions/uploads/preuves/")) {
            return false;
        }
        
        try {
            String filename = imageUrl.substring("/transactions/uploads/preuves/".length());
            String projectRoot = System.getProperty("user.dir");
            Path filePath = Paths.get(projectRoot, uploadDir, filename);
            
            return Files.deleteIfExists(filePath);
        } catch (Exception e) {
            System.err.println("❌ Erreur suppression fichier: " + e.getMessage());
            return false;
        }
    }
}