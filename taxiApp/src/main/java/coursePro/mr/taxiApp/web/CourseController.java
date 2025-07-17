package coursePro.mr.taxiApp.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import coursePro.mr.taxiApp.dto.CourseDto;
import coursePro.mr.taxiApp.dto.PassagerDto;
import coursePro.mr.taxiApp.entity.Course;
import coursePro.mr.taxiApp.enums.StatutCourse;
import coursePro.mr.taxiApp.mapper.CourseMapper;
import coursePro.mr.taxiApp.security.JwtService;
import coursePro.mr.taxiApp.service.CourseService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/courses")
@CrossOrigin
public class CourseController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private JwtService jwtService;

    //  @Autowired
    // private ConducteurService conducteurService;

    @PostMapping("/admin/createCourse")
    public ResponseEntity<?> createCourseByAdmin(@RequestBody CourseDto dto,HttpServletRequest request ) {
        try {
      
            CourseDto saved = courseService.createCourseByAdmin(dto);

            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la création de la course : " + e.getMessage());
        }
    }
     @PutMapping("/admin/updateCourse/{id}")
public ResponseEntity<?> updateCourseByAdmin(@RequestBody CourseDto dto, @PathVariable Long id) {
    try {
        // Validation de l'ID
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest()
                    .body("ID de course invalide");
        }
        
        // Validation du DTO
        if (dto == null) {
            return ResponseEntity.badRequest()
                    .body("Données de course manquantes");
        }
        
        // Appel du service avec le bon nom de méthode
        CourseDto updatedCourse = courseService.updateCourseByAdmin(dto, id);
        
        return ResponseEntity.ok(updatedCourse);
        
    } catch (IllegalArgumentException e) {
        // Erreurs de validation métier
        return ResponseEntity.badRequest()
                .body("Erreur de validation : " + e.getMessage());
                
    } catch (EntityNotFoundException e) {
        // Course non trouvée
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Course non trouvée : " + e.getMessage());
                
    } catch (Exception e) {
        // Erreur interne du serveur
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erreur lors de la mise à jour de la course : " + e.getMessage());
    }
}
    @DeleteMapping("/admin/delete/{id}")
public ResponseEntity<?> deleteCourseByAdmin(@PathVariable Long id) {
    try {
        // Validation de l'ID
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest()
                    .body("ID de course invalide");
        }
        
        // Appel du service pour supprimer la course
        boolean deleted = courseService.delete(id);
        
        if (deleted) {
            return ResponseEntity.ok()
                    .body("Course supprimée avec succès");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la suppression de la course");
        }
        
    } catch (IllegalArgumentException e) {
        // Erreurs de validation
        return ResponseEntity.badRequest()
                .body("Erreur de validation : " + e.getMessage());
                
    } catch (EntityNotFoundException e) {
        // Course non trouvée
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Course non trouvée : " + e.getMessage());
                
    } catch (IllegalStateException e) {
        // Course ne peut pas être supprimée
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Suppression impossible : " + e.getMessage());
                
    } catch (Exception e) {
        // Erreur interne du serveur
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erreur lors de la suppression de la course : " + e.getMessage());
    }
}
    
    @PostMapping("/passager/demander")
    public ResponseEntity<?> createCourse(@RequestBody CourseDto dto,HttpServletRequest request ) {
        try {
            String token = request.getHeader("Authorization").substring(7); // Supprime "Bearer "
        Long userId = jwtService.extractUserId(token); // Ton propre JwtService
            
        PassagerDto passager= new PassagerDto(userId);
           
            dto.setPassager(passager);
            
            CourseDto saved = courseService.save(dto);

           
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la création de la course : " + e.getMessage());
        }
    }
    @PutMapping("/conducteur/accepter/{id}")
public ResponseEntity<?> accepterCourse(@PathVariable Long id,HttpServletRequest request ) {
    try {
        System.out.println(id);
           String token = request.getHeader("Authorization").substring(7); // Supprime "Bearer "
        Long userId = jwtService.extractUserId(token); // Ton propre JwtService
          
        Course course = courseService.findById(id);
        
        // Vérifier que la course est encore en attente
        if (!course.getStatut().equals(StatutCourse.EN_ATTENTE)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("La course n'est plus disponible.");
        }
        courseService.accepterCourse(id,userId);
        // Mettre à jour le statut
       // course.setStatut(StatutCourse.ACCEPTEE);
        

        // Optionnel : ajouter le conducteur courant (si extrait du token)
        // Long conducteurId = jwtService.extractUserId(token); // via header
        // Conducteur conducteur = conducteurRepo.findByUtilisateur_Id(conducteurId).orElseThrow();
        // course.setConducteur(conducteur);

       // courseService.save(CourseMapper.toDto(course));

        return ResponseEntity.ok("Course acceptée");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Erreur lors de l'acceptation : " + e.getMessage());
    }
}

    // 🔍 Récupérer toutes les courses
    @GetMapping("/admin/readAll")
    public ResponseEntity<?> getAll() {
        try {
            List<CourseDto> list = courseService.findAll();
                    // .stream().map(CourseMapper::toDto)
                    // .collect(Collectors.toList());
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la récupération des courses : " + e.getMessage());
        }
    }

    // 🔍 Récupérer une course par ID
    @PostMapping("/admin/read")
    public ResponseEntity<?> getById(HttpServletRequest request) {
        try {
             String token = request.getHeader("Authorization").substring(7); // Supprime "Bearer "
        Long id = jwtService.extractUserId(token); // Ton propre JwtService
            
            return ResponseEntity.ok(CourseMapper.toDto(courseService.findById(id)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Course non trouvée : " + e.getMessage());
        }
    }

    // 🔍 Récupérer toutes les courses d’un passager
    @GetMapping("/passager/readAll")
    public ResponseEntity<?> getByPassager(HttpServletRequest request) {
        try {
                String token = request.getHeader("Authorization").substring(7); // Supprime "Bearer "
        Long passagerId = jwtService.extractUserId(token); // Ton propre JwtService
         
            List<CourseDto> list = courseService.findByPassagerId(passagerId);
                    // .stream().map(CourseMapper::toDto)
                    // .collect(Collectors.toList());
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la récupération des courses du passager : " + e.getMessage());
        }
    }

    // 🔍 Récupérer toutes les courses d’un conducteur
    @GetMapping("/conducteur/readAll")
    public ResponseEntity<?> getByConducteur(HttpServletRequest request) {
        try {
                String token = request.getHeader("Authorization").substring(7); // Supprime "Bearer "
        Long conducteurId = jwtService.extractUserId(token); // Ton propre JwtService
         
            List<CourseDto> list = courseService.findByConducteurId(conducteurId);
                    // .stream().map(CourseMapper::toDto)
                    // .collect(Collectors.toList());
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la récupération des courses du conducteur : " + e.getMessage());
        }
    }

    // 🗑️ Supprimer une course
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            courseService.delete(id);
            return ResponseEntity.ok("Course supprimée avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la suppression de la course : " + e.getMessage());
        }
    }

}

