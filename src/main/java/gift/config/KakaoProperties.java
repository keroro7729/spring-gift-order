package gift.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "kakao")
@Component
public class KakaoProperties {

    private String clientId;
    private String redirectLogin;

    public String getClientId() {
        return clientId;
    }

    public String getRedirectLogin() {
        return redirectLogin;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setRedirectLogin(String redirectLogin) {
        this.redirectLogin = redirectLogin;
    }
}
