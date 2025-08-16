package coursePro.mr.taxiApp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // ✅ Configuration pour servir les images depuis le dossier uploads
        String projectRoot = System.getProperty("user.dir");
        String uploadsPath = Paths.get(projectRoot, "uploads").toString() + File.separator;
        
        System.out.println("📁 Configuration serveur d'images: " + uploadsPath);
        
        // Créer le dossier s'il n'existe pas
        try {
            Files.createDirectories(Paths.get(uploadsPath, "preuves"));
            System.out.println("📁 Dossier preuves créé: " + Paths.get(uploadsPath, "preuves"));
        } catch (IOException e) {
            System.err.println("❌ Erreur création dossier: " + e.getMessage());
        }
        
        // Configuration du serveur de fichiers statiques
        registry.addResourceHandler("/api/uploads/**")
                .addResourceLocations("file:" + uploadsPath)
                .setCachePeriod(3600) // Cache 1 heure
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        Resource requestedResource = location.createRelative(resourcePath);
                        
                        // Sécurité: vérifier que le fichier existe et est dans le bon dossier
                        if (requestedResource.exists() && requestedResource.isReadable()) {
                            try {
                                String canonicalPath = requestedResource.getFile().getCanonicalPath();
                                String allowedPath = new File(uploadsPath).getCanonicalPath();
                                
                                if (canonicalPath.startsWith(allowedPath)) {
                                    return requestedResource;
                                }
                            } catch (IOException e) {
                                System.err.println("❌ Erreur vérification sécurité: " + e.getMessage());
                            }
                        }
                        return null;
                    }
                });
    }
    
    // ✅ Configuration CORS pour permettre l'accès aux images
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/uploads/**")
                .allowedOrigins("http://localhost:3000", "http://localhost:5173") // Vos URLs frontend
                .allowedMethods("GET")
                .allowedHeaders("*")
                .maxAge(3600);
    }
}
