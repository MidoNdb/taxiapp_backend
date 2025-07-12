package coursePro.mr.taxiApp.entity;

import java.time.LocalDateTime;

import coursePro.mr.taxiApp.enums.ModePaiement;
import coursePro.mr.taxiApp.enums.StatutCourse;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   @Column(name = "created_at" ,nullable=false ) 
    private LocalDateTime createdAt;

    @PrePersist
    public void setDateHeureNow() {
        this.createdAt = LocalDateTime.now();
    }
   
    
 @Column(nullable=false ) 
@Embedded
@jakarta.persistence.AttributeOverrides({
    @jakarta.persistence.AttributeOverride(name = "latitude", column = @jakarta.persistence.Column(name = "depart_latitude")),
    @jakarta.persistence.AttributeOverride(name = "longitude", column = @jakarta.persistence.Column(name = "depart_longitude")),
    @jakarta.persistence.AttributeOverride(name = "address", column = @jakarta.persistence.Column(name = "depart_address")),
    @jakarta.persistence.AttributeOverride(name = "place_name", column = @jakarta.persistence.Column(name = "depart_place_name"))
})

private Point depart;

@Column(nullable=false ) 
@Embedded
@jakarta.persistence.AttributeOverrides({
    @jakarta.persistence.AttributeOverride(name = "latitude", column = @jakarta.persistence.Column(name = "arrivee_latitude")),
    @jakarta.persistence.AttributeOverride(name = "longitude", column = @jakarta.persistence.Column(name = "arrivee_longitude")),
    @jakarta.persistence.AttributeOverride(name = "address", column = @jakarta.persistence.Column(name = "arrivee_address")),
    @jakarta.persistence.AttributeOverride(name = "place_name", column = @jakarta.persistence.Column(name = "arrivee_place_name"))
})
private Point arrivee;
     
    @Column(name = "pickup_time")
    private LocalDateTime pickupTime;
    @Column(name = "completion_time")
    private LocalDateTime completionTime;
    @Column(name = "estimated_duration")
    private Integer estimatedDuration;

    private Double distance;
    @Column(nullable=false ) 
    private Double montant;

     @Column(nullable=false ) 
    @Enumerated(EnumType.STRING)
    private StatutCourse statut;

    @Enumerated(EnumType.STRING)
    private ModePaiement modePaiement;

    
    @ManyToOne
    @JoinColumn(name = "passager")
    private Passager passager;

    @ManyToOne
    @JoinColumn(name = "conducteur")
    private Conducteur conducteur;

    @OneToOne(mappedBy = "course", cascade = CascadeType.ALL)
    private Evaluation evaluation;

    @OneToOne(mappedBy = "course", cascade = CascadeType.ALL)
    private Paiement paiement;

    public Course() {
    }

    // public Course(Long id, LocalDateTime dateHeure, Point depart, Point arrivee, Double distance,
    //         Double montant, StatutCourse statut, ModePaiement modePaiement, Passager passager, Conducteur conducteur,
    //         Evaluation evaluation, Paiement paiement) {
    //     this.id = id;
    //     this.dateHeure = dateHeure;
    //     this.depart = depart;
    //     this.arrivee = arrivee;
    //     this.distance = distance;
    //     this.montant = montant;
    //     this.statut = statut;
    //     this.modePaiement = modePaiement;
    //     this.passager = passager;
    //     this.conducteur = conducteur;
    //     this.evaluation = evaluation;
    //     this.paiement = paiement;
    // }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

   
    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public StatutCourse getStatut() {
        return statut;
    }

    public void setStatut(StatutCourse statut) {
        this.statut = statut;
    }

    public ModePaiement getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(ModePaiement modePaiement) {
        this.modePaiement = modePaiement;
    }

    public Passager getPassager() {
        return passager;
    }

    public void setPassager(Passager passager) {
        this.passager = passager;
    }

    public Conducteur getConducteur() {
        return conducteur;
    }

    public void setConducteur(Conducteur conducteur) {
        this.conducteur = conducteur;
    }

    public Evaluation getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
    }

    public Paiement getPaiement() {
        return paiement;
    }

    public void setPaiement(Paiement paiement) {
        this.paiement = paiement;
    }

    public Point getDepart() {
        return depart;
    }

    public void setDepart(Point depart) {
        this.depart = depart;
    }

    public Point getArrivee() {
        return arrivee;
    }

    public void setArrivee(Point arrivee) {
        this.arrivee = arrivee;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(LocalDateTime pickupTime) {
        this.pickupTime = pickupTime;
    }

    public LocalDateTime getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(LocalDateTime completionTime) {
        this.completionTime = completionTime;
    }

    public Integer getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(Integer estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

  
}

