package gift.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.DefaultConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
import org.apache.hc.client5.http.impl.classic.DefaultBackoffStrategy;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.impl.DefaultConnectionReuseStrategy;
import org.apache.hc.core5.util.TimeValue;

import java.util.concurrent.TimeUnit;


@Configuration
public class RestClientConfig {

    // 연결 풀(Connection Pool) 설정 값
    private static final int MAX_TOTAL_CONNECTIONS = 200;
    private static final int MAX_CONNECTIONS_PER_ROUTE = 20;
    private static final int MAX_IDLE_TIME = 30;

    // 재시도 설정 값
    private static final int MAX_RETRIES = 0;
    private static final long RETRY_INTERVAL_IN_SECONDS = 1L;

    // 타임아웃 설정 값
    private static final long RESPONSE_TIMEOUT = 5L;
    private static final long CONNECTION_REQUEST_TIMEOUT = 3L;

    @Bean(name = "kauthClient")
    public RestClient kauthClient(RestClient.Builder builder, HttpClient httpClient) {
        return RestClient.builder()
                .baseUrl("https://kauth.kakao.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8")
                .requestFactory(new HttpComponentsClientHttpRequestFactory(httpClient))
                .build();
    }

    @Bean(name = "kapiClient")
    public RestClient kapiClient(HttpClient httpClient) {
        return RestClient.builder()
                .baseUrl("https://kapi.kakao.com")
                .requestFactory(new HttpComponentsClientHttpRequestFactory(httpClient))
                .build();
    }

    @Bean
    public HttpClient httpClient() {
        return HttpClients.custom()
                .setConnectionManager(buildConnectionManager())
                .setConnectionReuseStrategy(DefaultConnectionReuseStrategy.INSTANCE)
                .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                .setRetryStrategy(buildRetryStrategy())
                .setConnectionBackoffStrategy(new DefaultBackoffStrategy())
                .setDefaultRequestConfig(requestConfig())
                .evictExpiredConnections()
                .evictIdleConnections(TimeValue.ofSeconds(MAX_IDLE_TIME))
                .build();
    }

    private PoolingHttpClientConnectionManager buildConnectionManager() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
        connectionManager.setDefaultMaxPerRoute(MAX_CONNECTIONS_PER_ROUTE);
        return connectionManager;
    }


    private RequestConfig requestConfig() {
        return RequestConfig.custom()
                .setResponseTimeout(RESPONSE_TIMEOUT, TimeUnit.SECONDS)
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    private DefaultHttpRequestRetryStrategy buildRetryStrategy() {
        return new DefaultHttpRequestRetryStrategy(
                MAX_RETRIES, TimeValue.ofSeconds(RETRY_INTERVAL_IN_SECONDS)
        );
    }
}