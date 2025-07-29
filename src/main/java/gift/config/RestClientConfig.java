package gift.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean(name = "kauthClient")
    public RestClient kauthClient(RestClient.Builder builder) {
        return builder
                .baseUrl("https://kauth.kakao.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8")
                .build();
    }

    @Bean(name = "kapiClient")
    public RestClient kapiClient(RestClient.Builder builder) {
        return builder
                .baseUrl("https://kapi.kakao.com")
                .build();
    }
}
