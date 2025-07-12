package coursePro.mr.taxiApp.dto;

import coursePro.mr.taxiApp.entity.Utilisateur;

public class EvaluationDto {
    
    public Long id;
    public int note;
    public String commentaire;
    public CourseDto course;
    public Utilisateur auteur;
    public Utilisateur cible;
  
    public EvaluationDto() {
    }
    public EvaluationDto(Long id, int note, String commentaire, CourseDto course, Utilisateur auteur,
            Utilisateur cible) {
        this.id = id;
        this.note = note;
        this.commentaire = commentaire;
        this.course = course;
        this.auteur = auteur;
        this.cible = cible;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getNote() {
        return note;
    }
    public void setNote(int note) {
        this.note = note;
    }
    public String getCommentaire() {
        return commentaire;
    }
    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
    public CourseDto getCourse() {
        return course;
    }
    public void setCourse(CourseDto course) {
        this.course = course;
    }
    public Utilisateur getAuteur() {
        return auteur;
    }
    public void setAuteur(Utilisateur auteur) {
        this.auteur = auteur;
    }
    public Utilisateur getCible() {
        return cible;
    }
    public void setCible(Utilisateur cible) {
        this.cible = cible;
    }
    
}

