package gift.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kakao")
public class KakaoProperties {

    private String clientId;
    private String redirectLogin;

    public KakaoProperties(String clientId, String redirectLogin) {
        this.clientId = clientId;
        this.redirectLogin = redirectLogin;
    }

    public String getClientId() {
        return clientId;
    }

    public String getRedirectLogin() {
        return redirectLogin;
    }
}
