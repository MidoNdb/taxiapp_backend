package coursePro.mr.taxiApp.model;

public class NotificationMessage {
    private String message;
    private Long utilisateurId;
    public NotificationMessage() {
    }
    public NotificationMessage(String message, Long utilisateurId) {
        this.message = message;
        this.utilisateurId = utilisateurId;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public Long getUtilisateurId() {
        return utilisateurId;
    }
    public void setUtilisateurId(Long utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    // Constructeurs, getters, setters
}

