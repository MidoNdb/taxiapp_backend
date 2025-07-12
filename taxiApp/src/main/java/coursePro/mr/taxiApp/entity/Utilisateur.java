package coursePro.mr.taxiApp.entity;

import java.util.List;

import coursePro.mr.taxiApp.enums.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "utilisateurs")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @Column(unique = true)
    private String telephone;
    
    @Column(nullable = false)
    private String motDePasse;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToOne(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    private Conducteur conducteur;

    @OneToOne(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    private Passager passager;

   
    // Relations
    
    
    @OneToMany(mappedBy = "auteur")
    private List<Evaluation> evaluationsEmises;

    @OneToMany(mappedBy = "cible")
    private List<Evaluation> evaluationsRecues;

    public Utilisateur() {
    }

    public Utilisateur(Long id, String nom,  String motDePasse, String telephone,
            Role role, Conducteur conducteur, Passager passager, List<Evaluation> evaluationsEmises,
            List<Evaluation> evaluationsRecues) {
        this.id = id;
        this.nom = nom;
        this.motDePasse = motDePasse;
        this.telephone = telephone;
        this.role = role;
        this.conducteur = conducteur;
        this.passager = passager;
        this.evaluationsEmises = evaluationsEmises;
        this.evaluationsRecues = evaluationsRecues;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Conducteur getConducteur() {
        return conducteur;
    }

    public void setConducteur(Conducteur conducteur) {
        this.conducteur = conducteur;
    }

    public Passager getPassager() {
        return passager;
    }

    public void setPassager(Passager passager) {
        this.passager = passager;
    }

    public List<Evaluation> getEvaluationsEmises() {
        return evaluationsEmises;
    }

    public void setEvaluationsEmises(List<Evaluation> evaluationsEmises) {
        this.evaluationsEmises = evaluationsEmises;
    }

    public List<Evaluation> getEvaluationsRecues() {
        return evaluationsRecues;
    }

    public void setEvaluationsRecues(List<Evaluation> evaluationsRecues) {
        this.evaluationsRecues = evaluationsRecues;
    }

    // Relations + Getters/Setters comme avant...
}
