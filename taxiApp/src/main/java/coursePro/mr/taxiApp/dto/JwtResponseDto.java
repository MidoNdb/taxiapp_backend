package coursePro.mr.taxiApp.dto;




public class JwtResponseDto {
    
    private String acssesToken;
    private String refreshToken;
    
    public JwtResponseDto() {
    }
    public JwtResponseDto(String acssesToken, String refreshToken) {
        this.acssesToken = acssesToken;
        this.refreshToken = refreshToken;
    }
    public String getAcssesToken() {
        return acssesToken;
    }
    public void setAcssesToken(String acssesToken) {
        this.acssesToken = acssesToken;
    }
    public String getRefreshToken() {
        return refreshToken;
    }
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
