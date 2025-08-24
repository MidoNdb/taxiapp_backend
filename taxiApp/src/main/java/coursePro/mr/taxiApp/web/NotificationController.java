// package coursePro.mr.taxiApp.web;

// import java.util.HashMap;
// import java.util.Map;

// import org.springframework.http.ResponseEntity;
// import org.springframework.messaging.simp.SimpMessagingTemplate;
// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.web.bind.annotation.PostMapping;

// import coursePro.mr.taxiApp.entity.TransactionWallet;

// @Controller
// @CrossOrigin(origins = "*")
// public class NotificationController {
    
//     private final SimpMessagingTemplate messagingTemplate;
//     //private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);
    
//     public NotificationController(SimpMessagingTemplate messagingTemplate) {
//         this.messagingTemplate = messagingTemplate;
//     }
    
    // // ✅ Méthode principale pour notifier les admins
    // public void notifyAdminRechargement(TransactionWallet transaction) {
    //     try {
    //         Map<String, Object> notification = new HashMap<>();
    //         notification.put("type", "NOUVEAU_RECHARGEMENT");
    //         notification.put("transactionId", transaction.getId());
    //         notification.put("montant", transaction.getMontant());
    //         notification.put("conducteurNom", transaction.getWallet().getConducteur().getUtilisateur().getNom());
    //         notification.put("conducteurTel", transaction.getWallet().getConducteur().getUtilisateur().getTelephone());
    //         notification.put("preuveUrl", transaction.getPreuveUrl());
    //         notification.put("dateDemande", transaction.getDate());
    //         notification.put("statut", transaction.getStatut().toString());
    //         notification.put("timestamp", System.currentTimeMillis());
            
    //         // ✅ Envoi vers tous les admins connectés
    //         messagingTemplate.convertAndSend("/topic/admin/rechargements", notification);
    //        // logger.info("✅ Notification admin rechargement envoyée");
            
    //     } catch (Exception e) {
    //        // logger.error("❌ Erreur notification admin rechargement: {}", e.getMessage(), e);
    //     }
    // }
    
//     // ✅ Endpoint de test pour vérifier les notifications
//     @PostMapping("/test")
//     public ResponseEntity<?> testNotification() {
//         try {
//             Map<String, Object> testNotification = new HashMap<>();
//             testNotification.put("type", "TEST");
//             testNotification.put("message", "Test de notification WebSocket");
//             testNotification.put("timestamp", System.currentTimeMillis());
            
//             messagingTemplate.convertAndSend("/topic/admin/rechargements", testNotification);
//             return ResponseEntity.ok("Notification de test envoyée");
//         } catch (Exception e) {
//             return ResponseEntity.status(500).body("Erreur: " + e.getMessage());
//         }
//     }
// }



// // @RestController
// // @RequestMapping("/notifications")
// // public class NotifController {

// //     @Autowired
// //     private JwtService jwtService;
// //         @Autowired
// //     private NotificationService notificationService;
// //     // --- Notification Endpoints ---
// //     @GetMapping("/notifications")
// //     public ResponseEntity<?> getAllNotifications() {
// //         try {
// //             return ResponseEntity.ok(notificationService.findAll());
// //         } catch (Exception e) {
// //             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
// //         }
// //     }

// //     @PostMapping("/notifications")
// //     public ResponseEntity<?> sendNotification(@RequestBody Notification n) {
// //         try {
// //             return ResponseEntity.ok(notificationService.save(n));
// //         } catch (Exception e) {
// //             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
// //         }
// //     }

// //     @GetMapping("/notifications/user/")
// //     public ResponseEntity<?> getUserNotifications(HttpServletRequest request) {
// //         try {
// //               String token = request.getHeader("Authorization").substring(7); // Supprime "Bearer "
// //         Long userId = jwtService.extractUserId(token); // Ton propre JwtService
         
// //             return ResponseEntity.ok(notificationService.findByUserId(userId));
// //         } catch (Exception e) {
// //             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
// //         }
// //     }

// // }
