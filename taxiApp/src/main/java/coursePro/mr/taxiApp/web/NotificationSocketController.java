package coursePro.mr.taxiApp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import coursePro.mr.taxiApp.dto.CourseDto;
import coursePro.mr.taxiApp.dto.NotificationDto;
import coursePro.mr.taxiApp.dto.RechargementNotificationDto;
import coursePro.mr.taxiApp.entity.TransactionWallet;
import coursePro.mr.taxiApp.mapper.ConducteurMapper;
import coursePro.mr.taxiApp.model.NotificationMessage;

@Controller
public class NotificationSocketController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationSocketController.class);
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
            
    public void notifyNewCourse(CourseDto course) {
        try {
            logger.info("🚗 Envoi notification nouvelle course - ID: {}", course.getId());
            
            NotificationDto notification = new NotificationDto();
            notification.setType("COURSE_REQUEST");
            notification.setCourse(course);
            
            logger.info("📡 Envoi vers /topic/driver/courses");
            logger.info("📄 Contenu notification: {}", notification);
            
            messagingTemplate.convertAndSend("/topic/driver/courses", notification);
            
            logger.info("✅ Notification envoyée avec succès");
            
        } catch (Exception e) {
            logger.error("❌ Erreur lors de l'envoi de la notification: {}", e.getMessage(), e);
        }
    }
   public void notifyAdminRechargement(TransactionWallet transaction) {
        try {
            logger.info("💰 Notification admin - Nouveau rechargement ID: {}", transaction.getId());
            
            RechargementNotificationDto notification = new RechargementNotificationDto();
            notification.setType("NOUVEAU_RECHARGEMENT");
            notification.setTransactionId(transaction.getId());
            notification.setMontant(transaction.getMontant());
            notification.setConducteur(ConducteurMapper.toDto(transaction.getWallet().getConducteur()));
            notification.setPreuveUrl(transaction.getPreuveUrl());
            notification.setDateDemande(transaction.getDate());
            notification.setStatut(transaction.getStatut().toString());
            
            // Envoi vers tous les admins connectés
            messagingTemplate.convertAndSend("/topic/admin/rechargements", notification);
            logger.info("✅ Notification admin rechargement envoyée");
            
        } catch (Exception e) {
            logger.error("❌ Erreur notification admin rechargement: {}", e.getMessage(), e);
        }
    }
    // envoyer à un utilisateur spécifique
    public void sendToUtilisateur(Long userId, NotificationMessage notification) {
        try {
            logger.info("👤 Envoi notification à l'utilisateur ID: {}", userId);
            messagingTemplate.convertAndSend("/topic/user/" + userId, notification);
            logger.info("✅ Notification utilisateur envoyée");
        } catch (Exception e) {
            logger.error("❌ Erreur envoi notification utilisateur: {}", e.getMessage(), e);
        }
    }
}