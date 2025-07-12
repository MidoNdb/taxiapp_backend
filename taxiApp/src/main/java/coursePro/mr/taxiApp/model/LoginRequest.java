package coursePro.mr.taxiApp.model;


public class LoginRequest {
    public String login;        // username ou téléphone
    public String motDePasse;
    
    public LoginRequest() {
    }
    public LoginRequest(String login, String motDePasse) {
        this.login = login;
        this.motDePasse = motDePasse;
    }
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getMotDePasse() {
        return motDePasse;
    }
    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
}

