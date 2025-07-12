package coursePro.mr.taxiApp.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "passagers")
public class Passager {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String adresse;

    @OneToOne
    @MapsId
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @OneToMany(mappedBy = "passager")
    private List<Course> coursesPassager;

    public Passager(Long id, String adresse, Utilisateur utilisateur, List<Course> coursesPassager) {
        this.id = id;
        this.adresse = adresse;
        this.utilisateur = utilisateur;
        this.coursesPassager = coursesPassager;
    }

    public Passager() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public List<Course> getCoursesPassager() {
        return coursesPassager;
    }

    public void setCoursesPassager(List<Course> coursesPassager) {
        this.coursesPassager = coursesPassager;
    }

}

