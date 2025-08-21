package coursePro.mr.taxiApp.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "payment_numbers")
public class PaymentNumber {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "numero_telephone", nullable = false, length = 8)
    private String numeroTelephone;
    
    @Column(name = "operateur", nullable = false, length = 20)
    private String operateur;
    
    @Column(name = "nom_proprietaire", length = 100)
    private String nomProprietaire;
    
    @Column(name = "actif", nullable = false)
    private Boolean actif = true;
    
    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();
    
    @Column(name = "date_modification")
    private LocalDateTime dateModification = LocalDateTime.now();
    
    // Constructeurs
    public PaymentNumber() {
    }
    
    public PaymentNumber(String numeroTelephone, String operateur, String nomProprietaire, Boolean actif) {
        this.numeroTelephone = numeroTelephone;
        this.operateur = operateur;
        this.nomProprietaire = nomProprietaire;
        this.actif = actif != null ? actif : true;
        this.dateCreation = LocalDateTime.now();
        this.dateModification = LocalDateTime.now();
    }
    
    public PaymentNumber(Long id, String numeroTelephone, String operateur, String nomProprietaire, 
                        Boolean actif, LocalDateTime dateCreation, LocalDateTime dateModification) {
        this.id = id;
        this.numeroTelephone = numeroTelephone;
        this.operateur = operateur;
        this.nomProprietaire = nomProprietaire;
        this.actif = actif;
        this.dateCreation = dateCreation;
        this.dateModification = dateModification;
    }
    
    // Méthodes JPA
    @PreUpdate
    public void preUpdate() {
        this.dateModification = LocalDateTime.now();
    }
    
    @PrePersist
    public void prePersist() {
        if (this.dateCreation == null) {
            this.dateCreation = LocalDateTime.now();
        }
        if (this.dateModification == null) {
            this.dateModification = LocalDateTime.now();
        }
        if (this.actif == null) {
            this.actif = true;
        }
    }
    
    // Getters et Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNumeroTelephone() {
        return numeroTelephone;
    }
    
    public void setNumeroTelephone(String numeroTelephone) {
        this.numeroTelephone = numeroTelephone;
    }
    
    public String getOperateur() {
        return operateur;
    }
    
    public void setOperateur(String operateur) {
        this.operateur = operateur;
    }
    
    public String getNomProprietaire() {
        return nomProprietaire;
    }
    
    public void setNomProprietaire(String nomProprietaire) {
        this.nomProprietaire = nomProprietaire;
    }
    
    public Boolean getActif() {
        return actif;
    }
    
    public void setActif(Boolean actif) {
        this.actif = actif;
    }
    
    public LocalDateTime getDateCreation() {
        return dateCreation;
    }
    
    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }
    
    public LocalDateTime getDateModification() {
        return dateModification;
    }
    
    public void setDateModification(LocalDateTime dateModification) {
        this.dateModification = dateModification;
    }
    
    // Builder Pattern
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private Long id;
        private String numeroTelephone;
        private String operateur;
        private String nomProprietaire;
        private Boolean actif = true;
        private LocalDateTime dateCreation;
        private LocalDateTime dateModification;
        
        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        
        public Builder numeroTelephone(String numeroTelephone) {
            this.numeroTelephone = numeroTelephone;
            return this;
        }
        
        public Builder operateur(String operateur) {
            this.operateur = operateur;
            return this;
        }
        
        public Builder nomProprietaire(String nomProprietaire) {
            this.nomProprietaire = nomProprietaire;
            return this;
        }
        
        public Builder actif(Boolean actif) {
            this.actif = actif;
            return this;
        }
        
        public Builder dateCreation(LocalDateTime dateCreation) {
            this.dateCreation = dateCreation;
            return this;
        }
        
        public Builder dateModification(LocalDateTime dateModification) {
            this.dateModification = dateModification;
            return this;
        }
        
        public PaymentNumber build() {
            PaymentNumber paymentNumber = new PaymentNumber();
            paymentNumber.setId(this.id);
            paymentNumber.setNumeroTelephone(this.numeroTelephone);
            paymentNumber.setOperateur(this.operateur);
            paymentNumber.setNomProprietaire(this.nomProprietaire);
            paymentNumber.setActif(this.actif);
            paymentNumber.setDateCreation(this.dateCreation != null ? this.dateCreation : LocalDateTime.now());
            paymentNumber.setDateModification(this.dateModification != null ? this.dateModification : LocalDateTime.now());
            return paymentNumber;
        }
    }
    
    // equals, hashCode et toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentNumber that = (PaymentNumber) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(numeroTelephone, that.numeroTelephone) &&
               Objects.equals(operateur, that.operateur);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, numeroTelephone, operateur);
    }
    
    @Override
    public String toString() {
        return "PaymentNumber{" +
                "id=" + id +
                ", numeroTelephone='" + numeroTelephone + '\'' +
                ", operateur='" + operateur + '\'' +
                ", nomProprietaire='" + nomProprietaire + '\'' +
                ", actif=" + actif +
                ", dateCreation=" + dateCreation +
                ", dateModification=" + dateModification +
                '}';
    }
}