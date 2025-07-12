package coursePro.mr.taxiApp.dto;

import coursePro.mr.taxiApp.enums.Role;

public class PassagerDto {
    public Long id;
    public String adresse;
    
    // Champs de l'utilisateur intégrés dans PassagerDto
    private String nom;
    private String telephone;
    private String motDePasse;
    private Role role;

    public PassagerDto() {}
    
    public PassagerDto(Long id, String adresse, String nom, String telephone, String motDePasse, Role role) {
        this.id = id;
        this.adresse = adresse;
        this.nom = nom;
        this.telephone = telephone;
        this.motDePasse = motDePasse;
        this.role = role;
    }
    
    public PassagerDto(Long id) {
        this.id = id;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    
    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
    
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}


