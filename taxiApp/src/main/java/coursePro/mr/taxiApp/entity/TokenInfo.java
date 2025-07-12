package coursePro.mr.taxiApp.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;



@Entity
public class TokenInfo {
     @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private  Long id;
    @NotBlank
    @Column(length=800)
    private String accessToken;
    @NotBlank
    @Column(length=800)
    private String refreshToken;
    private  String userAgentText;
    private  String localIpAddress;
    private  String remoteIpAddress;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id",referencedColumnName="id")
    private  Utilisateur user;

    public TokenInfo() {
    }

    public TokenInfo(Long id, @NotBlank String accessToken, @NotBlank String refreshToken, String userAgentText,
            String localIpAddress, String remoteIpAddress, Utilisateur user) {
        this.id = id;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userAgentText = userAgentText;
        this.localIpAddress = localIpAddress;
        this.remoteIpAddress = remoteIpAddress;
        this.user = user;
    }

    public TokenInfo(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getUserAgentText() {
        return userAgentText;
    }

    public void setUserAgentText(String userAgentText) {
        this.userAgentText = userAgentText;
    }

    public String getLocalIpAddress() {
        return localIpAddress;
    }

    public void setLocalIpAddress(String localIpAddress) {
        this.localIpAddress = localIpAddress;
    }

    public String getRemoteIpAddress() {
        return remoteIpAddress;
    }

    public void setRemoteIpAddress(String remoteIpAddress) {
        this.remoteIpAddress = remoteIpAddress;
    }

    public Utilisateur getUser() {
        return user;
    }

    public void setUser(Utilisateur user) {
        this.user = user;
    }

    
}

