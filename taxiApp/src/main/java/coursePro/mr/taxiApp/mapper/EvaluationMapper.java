package coursePro.mr.taxiApp.mapper;

import coursePro.mr.taxiApp.dto.EvaluationDto;
import coursePro.mr.taxiApp.entity.Evaluation;

public class EvaluationMapper {
    public static EvaluationDto toDto(Evaluation e) {
        if (e == null) return null;
        EvaluationDto dto = new EvaluationDto();
        dto.setId(e.getId());
        dto.setNote(e.getNote());
        dto.setCommentaire(e.getCommentaire());
        dto.setAuteur(e.getAuteur());
        dto.setCible(e.getCible());
        dto.setCourse(CourseMapper.toDto(e.getCourse()));
        return dto;
    }

    public static Evaluation toEntity(EvaluationDto dto) {
        if (dto == null) return null;
        Evaluation e = new Evaluation();
        e.setId(dto.getId());
        e.setNote(dto.getNote());
        e.setCommentaire(dto.getCommentaire());
        e.setAuteur(dto.getAuteur());
        e.setCible(dto.getCible());
        e.setCourse(CourseMapper.toEntity(dto.getCourse()));
        return e;
    }
}
