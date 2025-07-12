package coursePro.mr.taxiApp.service;

import java.util.List;

import coursePro.mr.taxiApp.entity.Utilisateur;

public interface UtilisateurService {
    Utilisateur findById(Long id);
    List<Utilisateur> findAll();
    Utilisateur save(Utilisateur utilisateur);
    void delete(Long id);
}

