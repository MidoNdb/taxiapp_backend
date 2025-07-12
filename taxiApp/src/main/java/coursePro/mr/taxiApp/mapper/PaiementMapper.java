package coursePro.mr.taxiApp.mapper;

import coursePro.mr.taxiApp.dto.PaiementDto;
import coursePro.mr.taxiApp.entity.Paiement;

public class PaiementMapper {
    public static PaiementDto toDto(Paiement p) {
        if (p == null) return null;
        PaiementDto dto = new PaiementDto();
        dto.setId(p.getId());
        dto.setMontant(p.getMontant());
        dto.setDate(p.getDate());
        dto.setTransactionId(p.getTransactionId());
        dto.setModePaiement(p.getModePaiement());
        dto.setCourse(CourseMapper.toDto(p.getCourse()));
        return dto;
    }

    public static Paiement toEntity(PaiementDto dto) {
        if (dto == null) return null;
        Paiement p = new Paiement();
        p.setId(dto.getId());
        p.setMontant(dto.getMontant());
        p.setDate(dto.getDate());
        p.setTransactionId(dto.getTransactionId());
        p.setModePaiement(dto.getModePaiement());
        p.setCourse(CourseMapper.toEntity(dto.getCourse()));
        return p;
    }
}
