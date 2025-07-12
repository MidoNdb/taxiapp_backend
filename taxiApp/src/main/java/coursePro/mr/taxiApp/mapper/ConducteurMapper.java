package coursePro.mr.taxiApp.mapper;

import coursePro.mr.taxiApp.dto.ConducteurDto;
import coursePro.mr.taxiApp.entity.Conducteur;
import coursePro.mr.taxiApp.entity.Utilisateur;

import coursePro.mr.taxiApp.entity.Point;

public class ConducteurMapper {
    public static ConducteurDto toDto(Conducteur c) {
        if (c == null) return null;
        return new ConducteurDto(
            
            c.getId(),
            c.getUtilisateur().getNom(),
            c.getUtilisateur().getTelephone(),
            "",
            c.getUtilisateur().getRole(),
            c.getNumeroPermis(),
            c.getVehicule(),
            c.getDisponible()
        );
    }

    public static Conducteur toEntity(ConducteurDto dto) {
        if (dto == null) return null;
        Conducteur c = new Conducteur();
        c.setId(dto.getId());
        if (dto.getNumeroPermis() != null) c.setNumeroPermis(dto.getNumeroPermis());
        if (dto.getVehicule() != null) c.setVehicule(dto.getVehicule());
        Utilisateur u= new Utilisateur();
        if(dto.getId()!= null){
            u.setId(dto.getId());
        }
         if(dto.getNom()!= null){
            u.setNom(dto.getNom());
        }
         if(dto.getTelephone()!= null){
            u.setTelephone(dto.getTelephone());
        }
         if(dto.getMotDePasse()!= null){
            u.setMotDePasse(dto.getMotDePasse());
        }
         if(dto.getRole()!= null){
            u.setRole(dto.getRole());
        }
         c.setUtilisateur(u);
         if (dto.getCurentPoint()!=null) {
            c.setCurentPosition(dto.getCurentPoint());  
         }
        return c;
    }
}
