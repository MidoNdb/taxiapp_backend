package coursePro.mr.taxiApp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir:uploads/preuves}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Permettre l'accès aux fichiers uploadés
        String baseDir = System.getProperty("user.dir");
        String fullUploadPath = "file:" + baseDir + File.separator + uploadDir + File.separator;
        
        registry.addResourceHandler("/uploads/preuves/**")
                .addResourceLocations(fullUploadPath);
    }
}