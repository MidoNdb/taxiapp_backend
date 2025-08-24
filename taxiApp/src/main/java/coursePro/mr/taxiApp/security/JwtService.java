package coursePro.mr.taxiApp.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.lang.Assert;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class JwtService {

 
   // @SuppressWarnings("deprecation")
   // private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    
    @Value("${jwt.secret}")
    private String secret;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
    

    private static Long ACCESS_TOKEN_VALIDITY;
    private static Long REFRESH_TOKEN_VALIDITY;

    public JwtService(
            @Value("${auth.expiration}") Long accessValidity,
            @Value("${auth.refresh.expiration}") Long refreshValidity) {
        Assert.notNull(accessValidity, "Access token validity must not be null");
        Assert.notNull(refreshValidity, "Refresh token validity must not be null");
        ACCESS_TOKEN_VALIDITY = accessValidity;
        REFRESH_TOKEN_VALIDITY = refreshValidity;
    }
  
    public String generateToken(String tel, String tokenId, String role, Long id, boolean isRefresh) {
        return Jwts.builder()
                .setId(tokenId)
                .setSubject(tel)
                .claim("role", role)  // Ajoute le rôle dans le token
                .claim("id", id)      // Ajoute l'ID utilisateur
                .setIssuedAt(new Date())
                .setIssuer("app-service")
                .setExpiration(calcTokenExpirationDate(isRefresh))
                //.signWith(SECRET_KEY)
                .signWith(getSigningKey())
                .compact();
    }
    

    private static Date calcTokenExpirationDate(boolean isRefresh) {
        return new Date(System.currentTimeMillis() + (isRefresh ? REFRESH_TOKEN_VALIDITY : ACCESS_TOKEN_VALIDITY) * 1000);
    }
  
    private Claims getClaims(String token) {
        return Jwts.parser()
        .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
    // Extrait l'username du token
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }
    public String getTokenIdFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.getId();
    }
    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }
    
    public Long extractUserId(String token) {
        return getClaims(token).get("id", Long.class);
    }
    
    // Vérifie si un token est valide
    public boolean isTokenValid(String token, UserDetailsImpl userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Vérifie si un token est expiré
    boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    public Boolean validateToken(String token, HttpServletRequest httpServletRequest) {
        try {
            Jwts.parser().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            System.out.println("Invalid signature");
        } catch (MalformedJwtException ex) {
            System.out.println("Malformed JWT");
        } catch (ExpiredJwtException ex) {
            httpServletRequest.setAttribute("expired", ex.getMessage());
            System.out.println("Token expired");
        } catch (UnsupportedJwtException ex) {
            System.out.println("Unsupported JWT");
        } catch (IllegalArgumentException ex) {
            System.out.println("Illegal argument");
        }
        return false;
    }

    
}
