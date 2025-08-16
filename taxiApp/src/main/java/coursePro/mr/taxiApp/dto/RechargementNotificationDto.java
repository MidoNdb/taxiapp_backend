package coursePro.mr.taxiApp.dto;

import java.time.LocalDateTime;

// RechargementNotificationDto.java
public class RechargementNotificationDto {
    private String type;
    private Long transactionId;
    private Double montant;
    private ConducteurDto conducteur; 
    private String preuveUrl;
    private LocalDateTime dateDemande;
    private String statut;
    
    // Constructeurs
    public RechargementNotificationDto() {}
    
    // Getters et Setters
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public Long getTransactionId() { return transactionId; }
    public void setTransactionId(Long transactionId) { this.transactionId = transactionId; }
    
    public Double getMontant() { return montant; }
    public void setMontant(Double montant) { this.montant = montant; }
    
    
    public String getPreuveUrl() { return preuveUrl; }
    public void setPreuveUrl(String preuveUrl) { this.preuveUrl = preuveUrl; }
    
    public LocalDateTime getDateDemande() { return dateDemande; }
    public void setDateDemande(LocalDateTime dateDemande) { this.dateDemande = dateDemande; }
    
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public ConducteurDto getConducteur() {
        return conducteur;
    }

    public void setConducteur(ConducteurDto conducteur) {
        this.conducteur = conducteur;
    }
}
