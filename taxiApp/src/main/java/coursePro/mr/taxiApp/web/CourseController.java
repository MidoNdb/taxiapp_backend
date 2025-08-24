package coursePro.mr.taxiApp.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import coursePro.mr.taxiApp.dto.ConducteurDto;
import coursePro.mr.taxiApp.dto.CourseDto;
import coursePro.mr.taxiApp.dto.PassagerDto;
import coursePro.mr.taxiApp.dto.WalletDto;
import coursePro.mr.taxiApp.entity.Course;
import coursePro.mr.taxiApp.enums.StatutCourse;
import coursePro.mr.taxiApp.mapper.CourseMapper;
import coursePro.mr.taxiApp.security.JwtService;
import coursePro.mr.taxiApp.service.CourseService;
import coursePro.mr.taxiApp.service.impls.WalletServiceImpl;
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
    @Autowired
    WalletServiceImpl walletService;
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
    
//     @PutMapping("/conducteur/accepter/{id}")
// public ResponseEntity<?> accepterCourse(@PathVariable Long id,HttpServletRequest request ) {
//     try {
//         System.out.println(id);
//            String token = request.getHeader("Authorization").substring(7); // Supprime "Bearer "
//         Long userId = jwtService.extractUserId(token); // Ton propre JwtService
          
//         Course course = courseService.findById(id);
        
//         // Vérifier que la course est encore en attente
//         if (!course.getStatut().equals(StatutCourse.EN_ATTENTE)) {
//             return ResponseEntity.status(HttpStatus.CONFLICT)
//                 .body("La course n'est plus disponible.");
//         }
//         courseService.accepterCourse(id,userId);
//         return ResponseEntity.ok("Course acceptée");
//     } catch (Exception e) {
//         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//             .body("Erreur lors de l'acceptation : " + e.getMessage());
//     }
// }

@PutMapping("/conducteur/accepter/{id}")
public ResponseEntity<?> accepterCourse(@PathVariable Long id, HttpServletRequest request) {
    try {
        System.out.println("Acceptation course ID: " + id);
        
        String token = request.getHeader("Authorization").substring(7);
        Long userId = jwtService.extractUserId(token);
        
        Course course = courseService.findById(id);
        
        if (course == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Course non trouvée"));
        }
        
        if (!course.getStatut().equals(StatutCourse.EN_ATTENTE)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", "La course n'est plus disponible"));
        }
        
        // Accepter la course
        courseService.accepterCourse(id, userId);
        
        // Retourner une réponse JSON structurée
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Course acceptée",
            "courseId", id
        ));
        
    } catch (Exception e) {
        e.printStackTrace(); // Pour debug
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of(
                "error", "Erreur lors de l'acceptation",
                "details", e.getMessage()
            ));
    }
}

    @PutMapping("/conducteur/{courseId}/status")
    
public ResponseEntity<?> updateCourseStatus(
        @PathVariable Long courseId,
        @RequestBody Map<String, String> request,
        HttpServletRequest httpRequest) {

    try {
        // ✅ 1. VÉRIFICATION DE L'AUTHENTIFICATION
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("error", "Authentification requise"));
        }

        // ✅ 2. VÉRIFICATION DU RÔLE CONDUCTEUR
        boolean hasConducteurRole = auth.getAuthorities().stream()
            .anyMatch(authority -> authority.getAuthority().equals("ROLE_CONDUCTEUR"));
        
        if (!hasConducteurRole) {
            return ResponseEntity.status(403).body(Map.of("error", "Accès interdit - Rôle CONDUCTEUR requis"));
        }

        // ✅ 3. EXTRACTION DE L'ID DU CONDUCTEUR CONNECTÉ
        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("error", "Token manquant"));
        }

        String token = authHeader.substring(7);
        Long conducteurConnecteId = jwtService.extractUserId(token);

        // ✅ 4. VÉRIFICATION QUE LA COURSE APPARTIENT AU CONDUCTEUR CONNECTÉ
        CourseDto course = courseService.getCourseById(courseId);
        if (course == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Course non trouvée"));
        }

        Long conducteurCourseId = course.getConducteur() != null ? course.getConducteur().getId() : null;

        if (conducteurCourseId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Aucun conducteur assigné à cette course"));
        }

        if (!conducteurConnecteId.equals(conducteurCourseId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", "Vous ne pouvez modifier que vos propres courses"));
        }

        // ✅ 5. VALIDATION DU STATUT
        String nouveauStatut = request.get("statut");
        if (nouveauStatut == null || nouveauStatut.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Le statut est requis"));
        }

        // ✅ 6. VÉRIFICATIONS SPÉCIALES POUR LE STATUT "EN_COURS"
        if ("EN_COURS".equalsIgnoreCase(nouveauStatut)) {
            // Vérifier que la course est dans le bon état pour être démarrée
            if (!"ACCEPTEE".equalsIgnoreCase(course.getStatut().toString())) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Seules les courses acceptées peuvent être démarrées"));
            }

            // Vérifier que le conducteur a un wallet suffisant
            ConducteurDto conducteurDto = new ConducteurDto(conducteurConnecteId);
            WalletDto walletDto = walletService.getOrCreateWallet(conducteurDto);
            
            // Calculer la commission
            double montantCourse = course.getMontant() != null ? course.getMontant() : 0.0;
            double commission = montantCourse * 0.07; // 7%
            
            if (!walletService.soldeSuffisant(walletDto, commission)) {
                return ResponseEntity.badRequest()
                    .body(Map.of(
                        "error", "Solde insuffisant pour démarrer la course",
                        "soldeActuel", walletDto.getSolde(),
                        "commissionRequise", commission
                    ));
            }
        }

        // ✅ 7. MISE À JOUR DU STATUT (avec déduction automatique de commission si EN_COURS)
        CourseDto courseUpdated = courseService.updateStatusCourse(courseId, nouveauStatut);

        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Statut de la course mis à jour avec succès",
            "course", courseUpdated
        ));

    } catch (EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of("error", "Course non trouvée"));

    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest()
            .body(Map.of("error", "Données invalides", "message", e.getMessage()));

    } catch (RuntimeException e) {
        return ResponseEntity.badRequest()
            .body(Map.of("error", e.getMessage()));

    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of("error", "Erreur interne du serveur"));
    }
}
// @PutMapping("/conducteur/{courseId}/status")
// //@PreAuthorize("hasRole('CONDUCTEUR')")
// public ResponseEntity<?> updateCourseStatus(
//         @PathVariable Long courseId,
//         @RequestBody Map<String, String> request) {
    
//     try {
//         String nouveauStatut = request.get("statut");
        
//         if (nouveauStatut == null || nouveauStatut.trim().isEmpty()) {
//             return ResponseEntity.badRequest().body(Map.of("error", "Le statut est requis"));
//         }
        
//         CourseDto courseUpdated = courseService.updateStatusCourse(courseId, nouveauStatut);
        
//         return ResponseEntity.ok(Map.of(
//             "success", true,
//             "message", "Statut de la course mis à jour avec succès",
//             "course", courseUpdated
//         ));
        
//     } catch (EntityNotFoundException e) {
//         return ResponseEntity.status(HttpStatus.NOT_FOUND)
//             .body(Map.of("error", "Course non trouvée", "message", e.getMessage()));
        
//     } catch (IllegalArgumentException e) {
//         return ResponseEntity.badRequest()
//             .body(Map.of("error", "Données invalides", "message", e.getMessage()));
        
//     } catch (Exception e) {
//         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//             .body(Map.of("error", "Erreur interne du serveur", "message", e.getMessage()));
//     }
// }

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

