package coursePro.mr.taxiApp.entity;

import java.util.List;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;


@Entity

@Table(name = "conducteurs")
public class Conducteur {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroPermis;
    private String vehicule;
    private Boolean disponible;
      @Embedded
@jakarta.persistence.AttributeOverrides({
    @jakarta.persistence.AttributeOverride(name = "latitude", column = @jakarta.persistence.Column(name = "depart_latitude")),
    @jakarta.persistence.AttributeOverride(name = "longitude", column = @jakarta.persistence.Column(name = "depart_longitude"))
})
private Point curentPosition;

    @OneToOne
     @MapsId
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    // Relation inverse (facultative)
    @OneToMany(mappedBy = "conducteur")
    private List<Course> coursesConducteur;

    public Conducteur(Long id, String numeroPermis, String vehicule, Boolean disponible, Utilisateur utilisateur,
            List<Course> coursesConducteur) {
        this.id = id;
        this.numeroPermis = numeroPermis;
        this.vehicule = vehicule;
        this.disponible = disponible;
        this.utilisateur = utilisateur;
        this.coursesConducteur = coursesConducteur;
    }

    public Conducteur() {
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

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public List<Course> getCoursesConducteur() {
        return coursesConducteur;
    }

    public void setCoursesConducteur(List<Course> coursesConducteur) {
        this.coursesConducteur = coursesConducteur;
    }

    public Point getCurentPosition() {
        return curentPosition;
    }

    public void setCurentPosition(Point curentPosition) {
        this.curentPosition = curentPosition;
    }

    
}

