package coursePro.mr.taxiApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import coursePro.mr.taxiApp.entity.Utilisateur;
import coursePro.mr.taxiApp.enums.Role;
import coursePro.mr.taxiApp.service.UtilisateurService;

@Component
public class StartUp implements CommandLineRunner {

    @Autowired
    private UtilisateurService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userService.findAll().isEmpty()) {
            Utilisateur admin = new Utilisateur();
            admin.setNom("admin");
            admin.setTelephone("12345678");
            admin.setMotDePasse(passwordEncoder.encode("admin"));
            admin.setRole(Role.ADMIN);

            userService.save(admin);
            System.out.println("✅ Utilisateur admin créé avec succès !");
        }
    }
}
