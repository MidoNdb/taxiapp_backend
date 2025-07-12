package coursePro.mr.taxiApp.mapper;

import coursePro.mr.taxiApp.dto.*;
import coursePro.mr.taxiApp.entity.*;
import java.time.LocalDateTime;

public class CourseMapper {

    public static CourseDto toDto(Course course) {
        if (course == null) return null;
        
        // Validation des points
        if (course.getDepart() == null) {
            throw new IllegalStateException("Point de départ invalide dans la course");
        }
        if (course.getArrivee() == null) {
            throw new IllegalStateException("Point d'arrivée invalide dans la course");
        }

        return new CourseDto(
            course.getId(),
            course.getCreatedAt(),
            course.getDepart(),
            course.getArrivee(),
            course.getPickupTime(),
            course.getCompletionTime(),
            course.getDistance(),
            course.getEstimatedDuration(),
            course.getMontant(),
            course.getStatut(),
            course.getModePaiement(),
            PassagerMapper.toDto(course.getPassager()),
            ConducteurMapper.toDto(course.getConducteur())
        );
    }

    public static Course toEntity(CourseDto dto) {
        if (dto == null) return null;
        
       // Validation des points avant conversion
        if (dto.getDepart() == null ) {
            throw new IllegalArgumentException("Point de départ invalide");
        }
        if (dto.getArrivee() == null) {
            throw new IllegalArgumentException("Point d'arrivée invalide");
        }

        Course course = new Course();
        
        course.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : LocalDateTime.now());
        
        if (dto.getPickupTime() != null) {
            course.setPickupTime(dto.getPickupTime());
        }
        
        if (dto.getCompletionTime() != null) {
            course.setCompletionTime(dto.getCompletionTime());
        }
        
        // Points géographiques (déjà validés)
        course.setDepart(dto.getDepart());
        course.setArrivee(dto.getArrivee());
        
        // Données numériques avec validation
        if (dto.getDistance() != null && dto.getDistance() > 0) {
            course.setDistance(dto.getDistance());
        }
        if (dto.getEstimatedDuration() != null && dto.getEstimatedDuration() > 0) {
            course.setEstimatedDuration(dto.getEstimatedDuration());
        }
        if (dto.getMontant() != null && dto.getMontant() > 0) {
            course.setMontant(dto.getMontant());
        }
        
        course.setStatut(dto.getStatut());
        course.setModePaiement(dto.getModePaiement());
        
        // Relations
        if (dto.getPassager() != null) {
            course.setPassager(PassagerMapper.toEntity(dto.getPassager()));
        }
        if (dto.getConducteur() != null) {
            course.setConducteur(ConducteurMapper.toEntity(dto.getConducteur()));
        }
        
        return course;
    }
}
// package coursePro.mr.taxiApp.mapper;

// import coursePro.mr.taxiApp.dto.*;
// import coursePro.mr.taxiApp.entity.*;

// import java.time.LocalDateTime;

// public class CourseMapper {

//     public static CourseDto toDto(Course course) {
//         if (course == null) return null;

//         return new CourseDto(
//             course.getId(),
//             course.getCreatedAt(),
//             course.getDepart(),
//             course.getArrivee(),
//             course.getPickupTime(),
//             course.getCompletionTime(),
//             course.getDistance(),
//             course.getEstimatedDuration(),
//             course.getMontant(),
//             course.getStatut(),
//             course.getModePaiement(),
//             PassagerMapper.toDto(course.getPassager()),
//             ConducteurMapper.toDto(course.getConducteur())
//         );
//     }

//     public static Course toEntity(CourseDto dto) {
//         if (dto == null) return null;

//         Course course = new Course();
        
//         // Dates avec gestion des valeurs par défaut
//         course.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : LocalDateTime.now());
        
//         if (dto.getPickupTime() != null) {
//             course.setPickupTime(dto.getPickupTime());
//         }
        
//         if (dto.getCompletionTime() != null) {
//             course.setCompletionTime(dto.getCompletionTime());
//         }
        
//         // Points géographiques
//         if (dto.getDepart() != null) {
//             course.setDepart(dto.getDepart());
//         }
        
//         if (dto.getArrivee() != null) {
//             course.setArrivee(dto.getArrivee());
//         }
        
//         // Données numériques
//         course.setDistance(dto.getDistance());
//         course.setEstimatedDuration(dto.getEstimatedDuration());
//         course.setMontant(dto.getMontant());
        
//         // Enums
//         course.setStatut(dto.getStatut());
//         course.setModePaiement(dto.getModePaiement());
        
//         // Relations - NE PAS mapper ici car cela peut créer des cycles
//         // Les relations seront définies dans le service
//         course.setPassager(PassagerMapper.toEntity(dto.getPassager()));
//         course.setConducteur(ConducteurMapper.toEntity(dto.getConducteur()));
        
//         return course;
//     }
// }
