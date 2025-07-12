package coursePro.mr.taxiApp.dto;

import java.time.LocalDateTime;

public class NotificationDto {
    private Long id;
    private String contenu;
    private String type; // e.g. COURSE_REQUEST
    private CourseDto course;
    private boolean lu;
    private LocalDateTime dateEnvoi;
    private UtilisateurDto utilisateur;
    public NotificationDto() {
    }
    public NotificationDto(Long id, String contenu, boolean lu, LocalDateTime dateEnvoi, UtilisateurDto utilisateur) {
        this.id = id;
        this.contenu = contenu;
        this.lu = lu;
        this.dateEnvoi = dateEnvoi;
        this.utilisateur = utilisateur;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getContenu() {
        return contenu;
    }
    public void setContenu(String contenu) {
        this.contenu = contenu;
    }
    public boolean isLu() {
        return lu;
    }
    public void setLu(boolean lu) {
        this.lu = lu;
    }
    public LocalDateTime getDateEnvoi() {
        return dateEnvoi;
    }
    public void setDateEnvoi(LocalDateTime dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }
    public UtilisateurDto getUtilisateur() {
        return utilisateur;
    }
    public void setUtilisateur(UtilisateurDto utilisateur) {
        this.utilisateur = utilisateur;
    }

    public CourseDto getCourse() {
        return course;
    }

    public void setCourse(CourseDto course) {
        this.course = course;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

