package coursePro.mr.taxiApp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import coursePro.mr.taxiApp.dto.ConducteurDto;
import coursePro.mr.taxiApp.entity.Point;
import coursePro.mr.taxiApp.security.JwtService;
import coursePro.mr.taxiApp.service.impls.ConducteurServiceImpl;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/conducteurs")
public class ConducteurController {
    
    @Autowired
    ConducteurServiceImpl conducteurService;

    @Autowired
    JwtService jwtService;

     @GetMapping("/admin/readAll")
    public ResponseEntity<?> getAllConducteurs() {
        try {
            return ResponseEntity.ok(conducteurService.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/conducteur/read")
    public ResponseEntity<?> getConducteur(HttpServletRequest request) {
        try {
             String token = request.getHeader("Authorization").substring(7); // Supprime "Bearer "
            Long id = jwtService.extractUserId(token); // Ton propre JwtService
            //System.out.println( "token ####   "+token);
            return ResponseEntity.ok(conducteurService.findById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/admin/add")
    public ResponseEntity<?> addConducteur(@RequestBody ConducteurDto conducteur) {
        try {
            return ResponseEntity.ok(conducteurService.save(conducteur));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/admin/update")
    public ResponseEntity<?> updateConducteur(@RequestBody ConducteurDto conducteur,HttpServletRequest request) {
        try {
                 String token = request.getHeader("Authorization").substring(7); // Supprime "Bearer "
            Long id = jwtService.extractUserId(token); // Ton propre JwtService
            
            return ResponseEntity.ok(conducteurService.update(conducteur, id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/admin/delete")
    public ResponseEntity<?> deleteConducteur(HttpServletRequest request) {
        try {
                  String token = request.getHeader("Authorization").substring(7); // Supprime "Bearer "
            Long id = jwtService.extractUserId(token); // Ton propre JwtService
            
            conducteurService.delete(id);
            return ResponseEntity.ok("Conducteur supprimé");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @PutMapping("/update/location")
    public ResponseEntity<?> updateLocation( @RequestBody Point point,HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization").substring(7); // Supprime "Bearer "
            Long id = jwtService.extractUserId(token); // Ton propre JwtService
            
            ConducteurDto updated = conducteurService.updateLocation(id, point);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur : " + e.getMessage());
        }
    }

}
