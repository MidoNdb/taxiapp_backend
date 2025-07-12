package coursePro.mr.taxiApp.dto;

import java.time.LocalDateTime;

import coursePro.mr.taxiApp.entity.Point;
import coursePro.mr.taxiApp.enums.ModePaiement;
import coursePro.mr.taxiApp.enums.StatutCourse;

public class CourseDto {
    public Long id;
    public LocalDateTime createdAt;
    public Point depart;
    public Point arrivee;
    
    private LocalDateTime pickupTime;
    private LocalDateTime completionTime;
    public Double distance;
    public Integer estimatedDuration; // AJOUTÉ pour estimated_duration
    public Double montant;
    public StatutCourse statut;
    public ModePaiement modePaiement;
    public PassagerDto passager;
    public ConducteurDto conducteur;

    public CourseDto() {}
    
    public CourseDto(Long id, LocalDateTime createdAt, Point depart, Point arrivee,
                    LocalDateTime pickupTime, LocalDateTime completionTime, Double distance,
                    Integer estimatedDuration, Double montant, StatutCourse statut, 
                    ModePaiement modePaiement, PassagerDto passager, ConducteurDto conducteur) {
        this.id = id;
        this.createdAt = createdAt;
        this.depart = depart;
        this.arrivee = arrivee;
        this.pickupTime = pickupTime;
        this.completionTime = completionTime;
        this.distance = distance;
        this.estimatedDuration = estimatedDuration;
        this.montant = montant;
        this.statut = statut;
        this.modePaiement = modePaiement;
        this.passager = passager;
        this.conducteur = conducteur;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public Point getDepart() { return depart; }
    public void setDepart(Point depart) { this.depart = depart; }
    
    public Point getArrivee() { return arrivee; }
    public void setArrivee(Point arrivee) { this.arrivee = arrivee; }
    
    public LocalDateTime getPickupTime() { return pickupTime; }
    public void setPickupTime(LocalDateTime pickupTime) { this.pickupTime = pickupTime; }
    
    public LocalDateTime getCompletionTime() { return completionTime; }
    public void setCompletionTime(LocalDateTime completionTime) { this.completionTime = completionTime; }
    
    public Double getDistance() { return distance; }
    public void setDistance(Double distance) { this.distance = distance; }
    
    public Integer getEstimatedDuration() { return estimatedDuration; }
    public void setEstimatedDuration(Integer estimatedDuration) { this.estimatedDuration = estimatedDuration; }
    
    public Double getMontant() { return montant; }
    public void setMontant(Double montant) { this.montant = montant; }
    
    public StatutCourse getStatut() { return statut; }
    public void setStatut(StatutCourse statut) { this.statut = statut; }
    
    public ModePaiement getModePaiement() { return modePaiement; }
    public void setModePaiement(ModePaiement modePaiement) { this.modePaiement = modePaiement; }
    
    public PassagerDto getPassager() { return passager; }
    public void setPassager(PassagerDto passager) { this.passager = passager; }
    
    public ConducteurDto getConducteur() { return conducteur; }
    public void setConducteur(ConducteurDto conducteur) { this.conducteur = conducteur; }

    @Override
    public String toString() {
        return "CourseDto{" +
                "depart=" + depart +
                ", arrivee=" + arrivee +
                ", distance=" + distance +
                ", estimatedDuration=" + estimatedDuration +
                ", montant=" + montant +
                ", statut=" + statut +
                ", modePaiement=" + modePaiement +
                ", passager=" + (passager != null ? passager.getId() : null) +
                '}';
    }
}