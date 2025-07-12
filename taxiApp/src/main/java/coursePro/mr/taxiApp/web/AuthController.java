package coursePro.mr.taxiApp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import coursePro.mr.taxiApp.dto.AccessTokenDto;
import coursePro.mr.taxiApp.dto.LoginResponse;
import coursePro.mr.taxiApp.model.LoginRequest;
import coursePro.mr.taxiApp.model.RefreshTokenRequest;
import coursePro.mr.taxiApp.model.RegisterRequest;
import coursePro.mr.taxiApp.security.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // JwtResponseDto token = authService.login(request);
        try {
            LoginResponse response = authService.login(request);
              if(!response.getRole().equals("ADMIN")){
                return ResponseEntity.ok(response);
            }else{
                
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("forbidden" );
            }
        } catch (Exception e) {
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de login : " + e.getMessage());
        }
      
    }
      @PostMapping("/login/admin")
    public ResponseEntity<?> loginAdmin(@RequestBody LoginRequest request) {
        // JwtResponseDto token = authService.login(request);
        try {
            
            LoginResponse response = authService.login(request);
            if(response.getRole().equals("ADMIN")){
                return ResponseEntity.ok(response);
            }else{
                
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("forbidden" );
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de login : " + e.getMessage());
        }
      
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@RequestBody RegisterRequest req){
        try {
            LoginResponse response = authService.register(req);
            
            return ResponseEntity.ok(response); 
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
        
    }

     @PostMapping("/refresh-token")
    public ResponseEntity<AccessTokenDto> refreshAccessToken(@RequestBody RefreshTokenRequest request) {
        AccessTokenDto accessTokenDto = authService.refreshAccessToken(request.getRefreshToken());

        if (accessTokenDto == null) {
            return ResponseEntity.badRequest().build(); // ou 401 Unauthorized si tu veux
        }

        return ResponseEntity.ok(accessTokenDto);
    }
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody RefreshTokenRequest request) {
        authService.logout(request.getRefreshToken());
        return ResponseEntity.ok().build();
    }
}

