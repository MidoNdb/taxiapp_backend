package coursePro.mr.taxiApp.dto;

import coursePro.mr.taxiApp.entity.Point;
import coursePro.mr.taxiApp.enums.Role;

public class ConducteurDto {
    
    public Long id;
    private String nom;

    private String telephone;
    
    private String motDePasse;

    private Role role;
    private String numeroPermis;
    private String vehicule;
    
    private Boolean disponible;
    private  Point curentPoint;
    
    public ConducteurDto(Long id){
        this.id=id;
    }
     
    public ConducteurDto(Long id,  String nom, String telephone, String motDePasse,
            Role role,String numeroPermis, String vehicule,Boolean disponible) {
        this.id = id;
        this.numeroPermis = numeroPermis;
        this.vehicule = vehicule;
        this.nom = nom;
        this.telephone = telephone;
        this.motDePasse = motDePasse;
        this.role = role;
        this.disponible=disponible;
    }
    public ConducteurDto() {
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public Point getCurentPoint() {
        return curentPoint;
    }

    public void setCurentPoint(Point curentPoint) {
        this.curentPoint = curentPoint;
    }
   
}

