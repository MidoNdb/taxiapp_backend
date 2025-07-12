package coursePro.mr.taxiApp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import coursePro.mr.taxiApp.dao.UtilisateurRepository;
import coursePro.mr.taxiApp.entity.Utilisateur;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UtilisateurRepository utilisateurRepo;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepo
            .findByTelephone(login)
            .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + login));

        return new UserDetailsImpl(utilisateur);
    }
}

