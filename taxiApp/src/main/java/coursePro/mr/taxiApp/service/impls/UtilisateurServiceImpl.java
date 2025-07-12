package coursePro.mr.taxiApp.service.impls;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coursePro.mr.taxiApp.dao.UtilisateurRepository;
import coursePro.mr.taxiApp.entity.Utilisateur;
import coursePro.mr.taxiApp.service.UtilisateurService;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

    @Autowired
    private UtilisateurRepository repo;

    @Override
    public Utilisateur findById(Long id) {
        return repo.findById(id).orElseThrow();
    }

    @Override
    public List<Utilisateur> findAll() {
        return repo.findAll();
    }

    @Override
    public Utilisateur save(Utilisateur utilisateur) {
        return repo.save(utilisateur);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }
}

