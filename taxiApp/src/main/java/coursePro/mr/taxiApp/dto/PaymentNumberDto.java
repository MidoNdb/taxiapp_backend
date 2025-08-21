package coursePro.mr.taxiApp.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class PaymentNumberDto {
    
    private Long id;
    
    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    @Size(min = 8, max = 8, message = "Le numéro doit contenir exactement 8 chiffres")
    @Pattern(regexp = "^[0-9]{8}$", message = "Le numéro doit contenir uniquement des chiffres")
    private String numeroTelephone;
    
    @NotBlank(message = "L'opérateur est obligatoire")
    @Size(max = 20, message = "Le nom de l'opérateur ne peut pas dépasser 20 caractères")
    private String operateur;
    
    @Size(max = 100, message = "Le nom du propriétaire ne peut pas dépasser 100 caractères")
    private String nomProprietaire;
    
    private Boolean actif = true;
    
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    
    // Champs calculés pour l'affichage mobile
    private String operateurAffichage;
    private String couleurOperateur;
    private String iconeOperateur;
    private String formatAffichage;
    private String instructions;
    private Boolean disponible;
    
    // Constructeurs
    public PaymentNumberDto() {
    }
    
    public PaymentNumberDto(String numeroTelephone, String operateur, String nomProprietaire, Boolean actif) {
        this.numeroTelephone = numeroTelephone;
        this.operateur = operateur;
        this.nomProprietaire = nomProprietaire;
        this.actif = actif != null ? actif : true;
    }
    
    public PaymentNumberDto(Long id, String numeroTelephone, String operateur, String nomProprietaire, 
                           Boolean actif, LocalDateTime dateCreation, LocalDateTime dateModification,
                           String operateurAffichage, String couleurOperateur, String iconeOperateur,
                           String formatAffichage, String instructions, Boolean disponible) {
        this.id = id;
        this.numeroTelephone = numeroTelephone;
        this.operateur = operateur;
        this.nomProprietaire = nomProprietaire;
        this.actif = actif;
        this.dateCreation = dateCreation;
        this.dateModification = dateModification;
        this.operateurAffichage = operateurAffichage;
        this.couleurOperateur = couleurOperateur;
        this.iconeOperateur = iconeOperateur;
        this.formatAffichage = formatAffichage;
        this.instructions = instructions;
        this.disponible = disponible;
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
    
    public String getOperateurAffichage() {
        return operateurAffichage;
    }
    
    public void setOperateurAffichage(String operateurAffichage) {
        this.operateurAffichage = operateurAffichage;
    }
    
    public String getCouleurOperateur() {
        return couleurOperateur;
    }
    
    public void setCouleurOperateur(String couleurOperateur) {
        this.couleurOperateur = couleurOperateur;
    }
    
    public String getIconeOperateur() {
        return iconeOperateur;
    }
    
    public void setIconeOperateur(String iconeOperateur) {
        this.iconeOperateur = iconeOperateur;
    }
    
    public String getFormatAffichage() {
        return formatAffichage;
    }
    
    public void setFormatAffichage(String formatAffichage) {
        this.formatAffichage = formatAffichage;
    }
    
    public String getInstructions() {
        return instructions;
    }
    
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
    
    public Boolean getDisponible() {
        return disponible;
    }
    
    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
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
        private String operateurAffichage;
        private String couleurOperateur;
        private String iconeOperateur;
        private String formatAffichage;
        private String instructions;
        private Boolean disponible;
        
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
        
        public Builder operateurAffichage(String operateurAffichage) {
            this.operateurAffichage = operateurAffichage;
            return this;
        }
        
        public Builder couleurOperateur(String couleurOperateur) {
            this.couleurOperateur = couleurOperateur;
            return this;
        }
        
        public Builder iconeOperateur(String iconeOperateur) {
            this.iconeOperateur = iconeOperateur;
            return this;
        }
        
        public Builder formatAffichage(String formatAffichage) {
            this.formatAffichage = formatAffichage;
            return this;
        }
        
        public Builder instructions(String instructions) {
            this.instructions = instructions;
            return this;
        }
        
        public Builder disponible(Boolean disponible) {
            this.disponible = disponible;
            return this;
        }
        
        public PaymentNumberDto build() {
            PaymentNumberDto dto = new PaymentNumberDto();
            dto.setId(this.id);
            dto.setNumeroTelephone(this.numeroTelephone);
            dto.setOperateur(this.operateur);
            dto.setNomProprietaire(this.nomProprietaire);
            dto.setActif(this.actif);
            dto.setDateCreation(this.dateCreation);
            dto.setDateModification(this.dateModification);
            dto.setOperateurAffichage(this.operateurAffichage);
            dto.setCouleurOperateur(this.couleurOperateur);
            dto.setIconeOperateur(this.iconeOperateur);
            dto.setFormatAffichage(this.formatAffichage);
            dto.setInstructions(this.instructions);
            dto.setDisponible(this.disponible);
            return dto;
        }
    }
    
    // equals, hashCode et toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentNumberDto that = (PaymentNumberDto) o;
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
        return "PaymentNumberDto{" +
                "id=" + id +
                ", numeroTelephone='" + numeroTelephone + '\'' +
                ", operateur='" + operateur + '\'' +
                ", nomProprietaire='" + nomProprietaire + '\'' +
                ", actif=" + actif +
                ", operateurAffichage='" + operateurAffichage + '\'' +
                ", disponible=" + disponible +
                '}';
    }
}