package coursePro.mr.taxiApp.entity;

import java.time.LocalDateTime;

import coursePro.mr.taxiApp.enums.ModePaiement;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "paiements")
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double montant;
    private LocalDateTime date;
    private String transactionId;

    @Enumerated(EnumType.STRING)
    private ModePaiement modePaiement;

    @OneToOne
    @JoinColumn(name = "course_id")
    private Course course;

    public Paiement(Long id, Double montant, LocalDateTime date, String transactionId, ModePaiement modePaiement,
            Course course) {
        this.id = id;
        this.montant = montant;
        this.date = date;
        this.transactionId = transactionId;
        this.modePaiement = modePaiement;
        this.course = course;
    }

    public Paiement() {
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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    // Getters/Setters...
}

