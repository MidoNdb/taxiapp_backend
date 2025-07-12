package coursePro.mr.taxiApp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import coursePro.mr.taxiApp.entity.Notification;
import coursePro.mr.taxiApp.security.JwtService;
import coursePro.mr.taxiApp.service.NotificationService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/notifications")
public class NotifController {

    @Autowired
    private JwtService jwtService;
        @Autowired
    private NotificationService notificationService;
    // --- Notification Endpoints ---
    @GetMapping("/notifications")
    public ResponseEntity<?> getAllNotifications() {
        try {
            return ResponseEntity.ok(notificationService.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/notifications")
    public ResponseEntity<?> sendNotification(@RequestBody Notification n) {
        try {
            return ResponseEntity.ok(notificationService.save(n));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/notifications/user/")
    public ResponseEntity<?> getUserNotifications(HttpServletRequest request) {
        try {
              String token = request.getHeader("Authorization").substring(7); // Supprime "Bearer "
        Long userId = jwtService.extractUserId(token); // Ton propre JwtService
         
            return ResponseEntity.ok(notificationService.findByUserId(userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
