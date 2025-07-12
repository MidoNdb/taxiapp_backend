package coursePro.mr.taxiApp.mapper;

import coursePro.mr.taxiApp.dto.PassagerDto;
import coursePro.mr.taxiApp.entity.Passager;
import coursePro.mr.taxiApp.entity.Utilisateur;


public class PassagerMapper {
    
    public static PassagerDto toDto(Passager p) {
        if (p == null) return null;
        
        // Gestion sécurisée des références nulles
        Utilisateur user = p.getUtilisateur();
        if (user == null) {
            return new PassagerDto(p.getId(), p.getAdresse(), null, null, null, null);
        }
        
        return new PassagerDto(
            p.getId(),
            p.getAdresse(),
            user.getNom(),
            user.getTelephone(),
            "", // Ne jamais exposer le mot de passe dans le DTO
            user.getRole()
        );
    }

    public static Passager toEntity(PassagerDto dto) {
        if (dto == null) return null;
        
        // Créer l'utilisateur
        Utilisateur utilisateur = new Utilisateur();
        if (dto.getId() != null) {
            utilisateur.setId(dto.getId());
        }
        if (dto.getNom() != null) {
            utilisateur.setNom(dto.getNom());
        }
        if (dto.getTelephone() != null) {
            utilisateur.setTelephone(dto.getTelephone());
        }
        if (dto.getMotDePasse() != null && !dto.getMotDePasse().isEmpty()) {
            utilisateur.setMotDePasse(dto.getMotDePasse());
        }
        if (dto.getRole() != null) {
            utilisateur.setRole(dto.getRole());
        }
        
        // Créer le passager
        Passager passager = new Passager();
        if (dto.getId() != null) {
            passager.setId(dto.getId());
        }
        if (dto.getAdresse() != null) {
            passager.setAdresse(dto.getAdresse());
        }
        
        // Établir la relation bidirectionnelle
        passager.setUtilisateur(utilisateur);
        utilisateur.setPassager(passager);
        
        return passager;
    }
    
    /**
     * Met à jour un passager existant avec les données du DTO
     */
    public static void updateEntity(Passager existingPassager, PassagerDto dto) {
        if (existingPassager == null || dto == null) return;
        
        // Mettre à jour les données du passager
        if (dto.getAdresse() != null) {
            existingPassager.setAdresse(dto.getAdresse());
        }
        
        // Mettre à jour les données de l'utilisateur
        Utilisateur user = existingPassager.getUtilisateur();
        if (user != null) {
            if (dto.getNom() != null) {
                user.setNom(dto.getNom());
            }
            if (dto.getTelephone() != null) {
                user.setTelephone(dto.getTelephone());
            }
            if (dto.getMotDePasse() != null && !dto.getMotDePasse().isEmpty()) {
                user.setMotDePasse(dto.getMotDePasse());
            }
            if (dto.getRole() != null) {
                user.setRole(dto.getRole());
            }
        }
    }
}