package coursePro.mr.taxiApp.dto;

import java.time.LocalDateTime;

import coursePro.mr.taxiApp.enums.ModePaiement;

public class PaiementDto {
    public Long id;
    public Double montant;
    public LocalDateTime date;
    public String transactionId;
    public ModePaiement modePaiement;
    public CourseDto course;

    public PaiementDto() {
    }
    public PaiementDto(Long id, Double montant, LocalDateTime date, String transactionId, ModePaiement modePaiement,
            CourseDto course) {
        this.id = id;
        this.montant = montant;
        this.date = date;
        this.transactionId = transactionId;
        this.modePaiement = modePaiement;
        this.course = course;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Double getMontant() {
        return montant;
    }
    public void setMontant(Double montant) {
        this.montant = montant;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public String getTransactionId() {
        return transactionId;
    }
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    public ModePaiement getModePaiement() {
        return modePaiement;
    }
    public void setModePaiement(ModePaiement modePaiement) {
        this.modePaiement = modePaiement;
    }

    public CourseDto getCourse() {
        return course;
    }

    public void setCourse(CourseDto course) {
        this.course = course;
    }

}

