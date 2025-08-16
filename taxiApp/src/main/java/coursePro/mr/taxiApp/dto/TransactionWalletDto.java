package coursePro.mr.taxiApp.dto;


import java.time.LocalDateTime;

import coursePro.mr.taxiApp.entity.Wallet;

public class TransactionWalletDto {
    private Long id;
    private Double montant;
    private String type;     // RECHARGEMENT, COMMISSION
    private String statut;   // EN_ATTENTE, VALIDEE, REJETEE
    private String preuveUrl;
    private LocalDateTime date;
    //public ConducteurDto conducteur;
    private WalletDto wallet;
    
    public TransactionWalletDto() {
    }
    public TransactionWalletDto(Long id, Double montant, String type, String statut, String preuveUrl,
            LocalDateTime date) {
        this.id = id;
        this.montant = montant;
        this.type = type;
        this.statut = statut;
        this.preuveUrl = preuveUrl;
        this.date = date;
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
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getStatut() {
        return statut;
    }
    public void setStatut(String statut) {
        this.statut = statut;
    }
    public String getPreuveUrl() {
        return preuveUrl;
    }
    public void setPreuveUrl(String preuveUrl) {
        this.preuveUrl = preuveUrl;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    // Getters & Setters

    public WalletDto getWallet() {
        return wallet;
    }

    public void setWallet(WalletDto wallet) {
        this.wallet = wallet;
    }

    // public ConducteurDto getConducteur() {
    //     return conducteur;
    // }

    // public void setConducteur(ConducteurDto conducteur) {
    //     this.conducteur = conducteur;
    // }
}

