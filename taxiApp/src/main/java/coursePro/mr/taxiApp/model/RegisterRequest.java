package coursePro.mr.taxiApp.model;

import coursePro.mr.taxiApp.enums.Role;

public class RegisterRequest {

    private String nom;
    private String telephone;
    private String motDePasse;
    private Role role;

    // Attributs spécifiques conducteur
    private String numeroPermis;
    private String vehicule;

    // Attribut spécifique passager
    private String adresse;

    public RegisterRequest() {
    }

    public RegisterRequest(String nom, String telephone,
            String motDePasse, Role role, String numeroPermis, String vehicule, String adresse) {
        this.nom = nom;
        this.telephone = telephone;
        this.motDePasse = motDePasse;
        this.role = role;
        this.numeroPermis = numeroPermis;
        this.vehicule = vehicule;
        this.adresse = adresse;
    }

    // Getters et Setters
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }


    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMotDePasse() {
        return motDePasse;
    }
    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }

    public String getNumeroPermis() {
        return numeroPermis;
    }
    public void setNumeroPermis(String numeroPermis) {
        this.numeroPermis = numeroPermis;
    }

    public String getVehicule() {
        return vehicule;
    }
    public void setVehicule(String vehicule) {
        this.vehicule = vehicule;
    }

    public String getAdresse() {
        return adresse;
    }
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
}
