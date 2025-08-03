package coursePro.mr.taxiApp.entity;


import java.time.LocalDateTime;

import coursePro.mr.taxiApp.enums.StatutTransaction;
import coursePro.mr.taxiApp.enums.TypeTransaction;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class TransactionWallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wallet", nullable = false)
    private Wallet wallet;

    private Double montant;

    @Enumerated(EnumType.STRING)
    private TypeTransaction type; // "RECHARGEMENT", "COMMISSION"

    @Enumerated(EnumType.STRING)
    private StatutTransaction statut; // "EN_ATTENTE", "VALIDEE", "REJETEE"

    @Column(name = "preuve_url")
    private String preuveUrl; // URL ou chemin vers la capture

    private LocalDateTime date = LocalDateTime.now();

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id){ this.id = id;}
    public Wallet getWallet() { return wallet; }
    public void setWallet(Wallet wallet) { this.wallet = wallet; }
    
    public Double getMontant() { return montant; }
    public void setMontant(Double montant) { this.montant = montant; }

    public TypeTransaction getType() { return type; }
    public void setType(TypeTransaction type) { this.type = type; }

    public StatutTransaction getStatut() { return statut; }
    public void setStatut(StatutTransaction statut) { this.statut = statut; }

    public String getPreuveUrl() { return preuveUrl; }
    public void setPreuveUrl(String preuveUrl) { this.preuveUrl = preuveUrl; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
}
