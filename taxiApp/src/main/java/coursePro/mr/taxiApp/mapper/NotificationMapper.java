package coursePro.mr.taxiApp.mapper;

import java.time.LocalDateTime;

import coursePro.mr.taxiApp.dto.NotificationDto;
import coursePro.mr.taxiApp.entity.Notification;

public class NotificationMapper {

    public static NotificationDto toDto(Notification n) {
        NotificationDto dto = new NotificationDto();
        dto.setId(n.getId());
        dto.setContenu(n.getContenu());
        dto.setLu(n.isLu());
        dto.setDateEnvoi(n.getDateEnvoi());
        dto.setUtilisateur(UtilisateurMapper.toDto(n.getUtilisateur()));
        return dto;
    }

    public static Notification toEntity(NotificationDto dto) {
        Notification n = new Notification();
        n.setContenu(dto.getContenu());
        n.setLu(dto.isLu());
        n.setDateEnvoi(dto.getDateEnvoi() != null ? dto.getDateEnvoi() : LocalDateTime.now());
        n.setUtilisateur(UtilisateurMapper.toEntity(dto.getUtilisateur()));
        return n;
    }
}

