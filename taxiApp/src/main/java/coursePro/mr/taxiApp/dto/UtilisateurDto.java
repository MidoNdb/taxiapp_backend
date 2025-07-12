package coursePro.mr.taxiApp.dto;

import coursePro.mr.taxiApp.enums.Role;

public class UtilisateurDto {
    
    public Long id;
    public String nom;
    public String telephone;
    public Role role;

    public UtilisateurDto() {
    }
    public UtilisateurDto(Long id, String nom, String telephone,
            Role role) {
        this.id = id;
        this.nom = nom;
        this.telephone = telephone;
        this.role = role;
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
}

// public class RegisterRequest {
//     public String nom;
//     public String prenom;
//     public String email;
//     public String username;
//     public String telephone;
//     public String motDePasse;
//     public Role role;
// }

// public class LoginRequest {
//     public String login; // username ou téléphone
//     public String motDePasse;
// }
