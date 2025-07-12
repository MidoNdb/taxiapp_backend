package coursePro.mr.taxiApp.mapper;

import coursePro.mr.taxiApp.dto.UtilisateurDto;
import coursePro.mr.taxiApp.entity.Utilisateur;

public class UtilisateurMapper {
    public static UtilisateurDto toDto(Utilisateur user) {
        if (user == null) return null;
        return new UtilisateurDto(
            user.getId(),
            user.getNom(),
            user.getTelephone(),
            user.getRole()
        );
    }

    public static Utilisateur toEntity(UtilisateurDto dto) {
        if (dto == null) return null;
        Utilisateur u = new Utilisateur();
        u.setId(dto.getId());
        u.setNom(dto.getNom());
        u.setTelephone(dto.getTelephone());
        u.setRole(dto.getRole());
        return u;
    }
}

