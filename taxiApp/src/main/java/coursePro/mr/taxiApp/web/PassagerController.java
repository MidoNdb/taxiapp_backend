package coursePro.mr.taxiApp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import coursePro.mr.taxiApp.dto.PassagerDto;
import coursePro.mr.taxiApp.security.JwtService;
import coursePro.mr.taxiApp.service.impls.PassagerServiceImpl;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/passagers")
public class PassagerController {
     
    @Autowired
    private PassagerServiceImpl passagerService;


    @Autowired
    private JwtService jwtService;


    @GetMapping("/admin/readAll")
    public ResponseEntity<?> getAllPassagers() {
        try {
            return ResponseEntity.ok(passagerService.findAllPassager());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/passager/read")
    public ResponseEntity<?> getPassager(HttpServletRequest request) {
        try {
             String token = request.getHeader("Authorization").substring(7); // Supprime "Bearer "
            Long id = jwtService.extractUserId(token); // Ton propre JwtService
            
            return ResponseEntity.ok(passagerService.findById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/admin/add")
    public ResponseEntity<?> addPassager(@RequestBody PassagerDto dto) {
        try {
            return ResponseEntity.ok(passagerService.save(dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/admin/update")
    public ResponseEntity<?> updatePassager(@RequestBody PassagerDto dto,HttpServletRequest request ) {
        try {
            String token = request.getHeader("Authorization").substring(7); // Supprime "Bearer "
            Long id = jwtService.extractUserId(token); // Ton propre JwtService
            
            return ResponseEntity.ok(passagerService.update(dto, id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/admin/delete")
    public ResponseEntity<?> deletePassager(HttpServletRequest request) {
        try {
             String token = request.getHeader("Authorization").substring(7); // Supprime "Bearer "
            Long id = jwtService.extractUserId(token); // Ton propre JwtService
            
            passagerService.delete(id);
            return ResponseEntity.ok("Passager supprimé");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    
}
