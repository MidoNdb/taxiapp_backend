package coursePro.mr.taxiApp.dto;


import java.time.LocalDateTime;
import java.util.List;

public class WalletDto {
    private Long id;
    private ConducteurDto conducteur;
    private Double solde;
    private Boolean actif;
    private LocalDateTime dernierRechargement;
    private List<TransactionWalletDto> transactions;
    
    public WalletDto() {
    }
    
    public WalletDto(Long id, ConducteurDto conducteur, Double solde, Boolean actif, LocalDateTime dernierRechargement,
            List<TransactionWalletDto> transactions) {
        this.id = id;
        this.conducteur = conducteur;
        this.solde = solde;
        this.actif = actif;
        this.dernierRechargement = dernierRechargement;
        this.transactions = transactions;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    
    public Double getSolde() {
        return solde;
    }
    public void setSolde(Double solde) {
        this.solde = solde;
    }
    public Boolean getActif() {
        return actif;
    }
    public void setActif(Boolean actif) {
        this.actif = actif;
    }
    public LocalDateTime getDernierRechargement() {
        return dernierRechargement;
    }
    public void setDernierRechargement(LocalDateTime dernierRechargement) {
        this.dernierRechargement = dernierRechargement;
    }
    public List<TransactionWalletDto> getTransactions() {
        return transactions;
    }
    public void setTransactions(List<TransactionWalletDto> transactions) {
        this.transactions = transactions;
    }

    // Getters & Setters

    public ConducteurDto getConducteur() {
        return conducteur;
    }

    public void setConducteur(ConducteurDto conducteur) {
        this.conducteur = conducteur;
    }
}

