package coursePro.mr.taxiApp.security;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import coursePro.mr.taxiApp.dao.ConducteurRepository;
import coursePro.mr.taxiApp.dao.PassagerRepository;
import coursePro.mr.taxiApp.dao.UtilisateurRepository;
import coursePro.mr.taxiApp.dto.AccessTokenDto;
import coursePro.mr.taxiApp.dto.JwtResponseDto;
import coursePro.mr.taxiApp.dto.LoginResponse;
import coursePro.mr.taxiApp.entity.Conducteur;
import coursePro.mr.taxiApp.entity.Passager;
import coursePro.mr.taxiApp.entity.TokenInfo;
import coursePro.mr.taxiApp.entity.Utilisateur;
import coursePro.mr.taxiApp.enums.Role;
import coursePro.mr.taxiApp.model.LoginRequest;
import coursePro.mr.taxiApp.model.RegisterRequest;
import coursePro.mr.taxiApp.service.impls.TokenInfoServiceImpl;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthService {
    @Autowired
    private  UtilisateurRepository utilisateurRepo;
    @Autowired
    private TokenInfoServiceImpl tokenInfoService;
    @Autowired
    private  JwtService jwtService;

    @Autowired
private ConducteurRepository conducteurRepo;

@Autowired
private PassagerRepository passagerRepo;

private final PasswordEncoder passwordEncoder;

private final HttpServletRequest httpRequest;

@Autowired
public AuthService(PasswordEncoder passwordEncoder, HttpServletRequest httpRequest) {
    this.passwordEncoder = passwordEncoder;
    this.httpRequest = httpRequest;
}

public LoginResponse register(RegisterRequest req) {
    if (utilisateurRepo.existsByTelephone((req.getTelephone()))) {
        throw new RuntimeException("Username déjà utilisé");
    }
    if (utilisateurRepo.existsByTelephone(req.getTelephone())) {
        throw new RuntimeException("Téléphone déjà utilisé");
    }

    Utilisateur utilisateur = new Utilisateur();
    utilisateur.setNom(req.getNom());
    utilisateur.setTelephone(req.getTelephone());
    utilisateur.setMotDePasse(passwordEncoder.encode(req.getMotDePasse()));
    utilisateur.setRole(req.getRole());
    
    utilisateurRepo.save(utilisateur);
    
    // Puis pour Conducteur ou Passager
    if (req.getRole() == Role.CONDUCTEUR) {
        Conducteur conducteur = new Conducteur();
        conducteur.setUtilisateur(utilisateur);
        conducteur.setNumeroPermis(req.getNumeroPermis());
        conducteur.setVehicule(req.getVehicule());
        conducteur.setDisponible(true);
        conducteurRepo.save(conducteur);
    }
    
    if (req.getRole() == Role.PASSAGER) {
        Passager passager = new Passager();
        passager.setUtilisateur(utilisateur);
        passager.setAdresse(req.getAdresse());
        passagerRepo.save(passager);
    }
    

    TokenInfo tokenInfo = createAndSaveToken(
        utilisateur.getTelephone(),
        utilisateur.getId(),
        utilisateur.getRole().name()
    );
    LoginResponse response = new LoginResponse();
    response.setAccessToken(tokenInfo.getAccessToken());
    response.setRefreshToken(tokenInfo.getRefreshToken());
    response.setUserId(jwtService.extractUserId(tokenInfo.getAccessToken()));
    response.setRole(jwtService.extractRole(tokenInfo.getAccessToken()));
    response.setUsername(jwtService.extractUsername(tokenInfo.getAccessToken()));


    return response;
}


    public LoginResponse login(LoginRequest req) {
        Utilisateur utilisateur = utilisateurRepo
                .findByTelephone(req.login)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        if (!passwordEncoder.matches(req.motDePasse, utilisateur.getMotDePasse())) {
            throw new BadCredentialsException("Mot de passe incorrect");
        }

        TokenInfo tokenInfo = createAndSaveToken(utilisateur.getTelephone(), utilisateur.getId(), utilisateur.getRole().name());
        
        LoginResponse response= new LoginResponse();
        response.setAccessToken(tokenInfo.getAccessToken());
        response.setRefreshToken(tokenInfo.getRefreshToken());
        response.setUserId(jwtService.extractUserId(tokenInfo.getAccessToken()));
        response.setRole(jwtService.extractRole(tokenInfo.getAccessToken()));
        response.setUsername(jwtService.extractUsername(tokenInfo.getAccessToken()));

        return response;
    }

   

    private TokenInfo createAndSaveToken(String tel, Long userId, String role) {
        String userAgent = httpRequest.getHeader(HttpHeaders.USER_AGENT);
        String remoteIp = httpRequest.getRemoteAddr();
        String localIp = "UNKNOWN";
    
        try {
            localIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            // Optionnel : enregistrer l'erreur dans les logs
        }
    
        // Générer AccessToken
        String accessTokenId = UUID.randomUUID().toString();
        String accessToken = jwtService.generateToken(tel, accessTokenId, role, userId, false);
    
        // Générer RefreshToken
        String refreshTokenId = UUID.randomUUID().toString();
        String refreshToken = jwtService.generateToken(tel, refreshTokenId, role, userId, true);
    
        // Construire l'objet TokenInfo
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setAccessToken(accessToken);
        tokenInfo.setRefreshToken(refreshToken);
    
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(userId);
        tokenInfo.setUser(utilisateur);
    
        tokenInfo.setUserAgentText(userAgent);
        tokenInfo.setLocalIpAddress(localIp);
        tokenInfo.setRemoteIpAddress(remoteIp);
    
        // Sauvegarder en base de données
        return tokenInfoService.save(tokenInfo);
    }
    

    public AccessTokenDto refreshAccessToken(String refreshToken) {
        // Vérifie si le token est expiré
        if (jwtService.isTokenExpired(refreshToken)) {
            return null;
        }
    
        // Vérifie si le refreshToken existe dans la base
        Optional<TokenInfo> optionalTokenInfo = tokenInfoService.findTokenInfo(refreshToken);
        if (optionalTokenInfo.isEmpty()) {
            return null;
        }
    
        // Extraire les informations du refreshToken
        String username = jwtService.extractUsername(refreshToken);
        String role = jwtService.extractRole(refreshToken);
        Long userId = jwtService.extractUserId(refreshToken);
    
        // Générer un nouveau AccessToken
        String newAccessToken = jwtService.generateToken(username, UUID.randomUUID().toString(), role, userId, false);
    
        return new AccessTokenDto(newAccessToken);
    }
    public void logout(String refreshToken) {
        Optional<TokenInfo> optionalTokenInfo = tokenInfoService.findTokenInfo(refreshToken);
        optionalTokenInfo.ifPresent(token -> tokenInfoService.deleteById(token.getId()));
    }
}

