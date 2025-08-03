package coursePro.mr.taxiApp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "conducteur_id", nullable = false, unique = true)
    private Conducteur conducteur;

    private Double solde = 0.0;

    private Boolean actif = false;

    @Column(name = "dernier_rechargement")
    private LocalDateTime dernierRechargement;

    // ✅ Liste des transactions liées à ce wallet
    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TransactionWallet> transactions = new ArrayList<>();

    // ======= Getters & Setters =======
    public Long getId() { return id; }
    public void setId(Long id){this.id = id;}

    public Conducteur getConducteur() { return conducteur; }
    public void setConducteur(Conducteur conducteur) { this.conducteur = conducteur; }

    public Double getSolde() { return solde; }
    public void setSolde(Double solde) { this.solde = solde; }

    public Boolean getActif() { return actif; }
    public void setActif(Boolean actif) { this.actif = actif; }

    public LocalDateTime getDernierRechargement() { return dernierRechargement; }
    public void setDernierRechargement(LocalDateTime dernierRechargement) {
        this.dernierRechargement = dernierRechargement;
    }

    public List<TransactionWallet> getTransactions() { return transactions; }
    public void setTransactions(List<TransactionWallet> transactions) { this.transactions = transactions; }
}
