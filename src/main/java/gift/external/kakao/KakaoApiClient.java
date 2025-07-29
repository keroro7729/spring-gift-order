package gift.external.kakao;

import gift.common.exception.BusinessException;
import gift.common.exception.code.ExternalErrorCode;
import gift.config.KakaoProperties;
import gift.external.kakao.request.GetTokenRequestDto;
import gift.external.kakao.request.RefreshRequestDto;
import gift.external.kakao.response.GetMemberIdResponseDto;
import gift.external.kakao.response.GetTokenResponseDto;
import gift.external.kakao.response.RefreshResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class KakaoApiClient {

    private static final Logger log = LoggerFactory.getLogger(KakaoApiClient.class);
    private final KakaoProperties properties;
    private RestClient.Builder builder;
    private final String KAKAO_AUTH_URL;

    public KakaoApiClient(RestClient.Builder builder, KakaoProperties properties) {
        this.builder = builder;
        this.properties = properties;
        KAKAO_AUTH_URL = "https://kauth.kakao.com/oauth/authorize?" +
                "client_id=" + properties.getClientId() +
                "&redirect_uri=" + properties.getRedirectLogin() +
                "&response_type=code";
    }

    public ResponseEntity<GetTokenResponseDto> requestToken(String code) {
        RestClient client = builder.baseUrl("https://kauth.kakao.com").build();
        GetTokenRequestDto request = GetTokenRequestDto.of(properties.getClientId(), properties.getRedirectLogin(), code);
        try {
            return client.post()
                    .uri("/oauth/token")
                    .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8")
                    .body(request.toBodyForm())
                    .retrieve()
                    .toEntity(GetTokenResponseDto.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("KAKAO/oauth/token fail detail: ", e);
            throw BusinessException.internal(ExternalErrorCode.KAKAO_OAUTH_TOKEN_FAIL,
                    "KAKAO/oauth/token 요청 실패: " + e.getMessage());
        } catch (RestClientException e) {
            log.error("KAKAO/oauth/token fail detail: ", e);
            throw BusinessException.internal(ExternalErrorCode.KAKAO_OAUTH_TOKEN_NETWORK_FAIL,
                    "네트워크 오류: " + e.getMessage());
        }
    }

    public ResponseEntity<GetMemberIdResponseDto> requestMemberId(String accessToken) {
        RestClient client = builder.baseUrl("https://kapi.kakao.com").build();
        try {
            return client.get()
                    .uri("/v1/user/access_token_info")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .toEntity(GetMemberIdResponseDto.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("KAKAO/oauth/token fail detail: ", e);
            log.error("Check accessToken!!: " + accessToken);
            throw BusinessException.internal(ExternalErrorCode.KAKAO_OAUTH_TOKEN_FAIL,
                    "KAKAO/oauth/token 요청 실패: " + e.getMessage());
        } catch (RestClientException e) {
            log.error("KAKAO/oauth/token fail detail: ", e);
            throw BusinessException.internal(ExternalErrorCode.KAKAO_OAUTH_TOKEN_NETWORK_FAIL,
                    "네트워크 오류: " + e.getMessage());
        }
    }

    public ResponseEntity<RefreshResponseDto> requestRefresh(String refreshToken) {
        RestClient client = builder.baseUrl("https://kauth.kakao.com").build();
        RefreshRequestDto request = RefreshRequestDto.of(properties.getClientId(), refreshToken);
        try {
            return client.post()
                    .uri("/oauth/token")
                    .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8")
                    .body(request.toBodyForm())
                    .retrieve()
                    .toEntity(RefreshResponseDto.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("KAKAO/oauth/token fail detail: ", e);
            throw BusinessException.internal(ExternalErrorCode.KAKAO_OAUTH_TOKEN_FAIL,
                    "KAKAO/oauth/token 요청 실패: " + e.getMessage());
        } catch (RestClientException e) {
            log.error("KAKAO/oauth/token fail detail: ", e);
            throw BusinessException.internal(ExternalErrorCode.KAKAO_OAUTH_TOKEN_NETWORK_FAIL,
                    "네트워크 오류: " + e.getMessage());
        }
    }

    public ResponseEntity<Void> redirectToKakaoAuth() {
        String redirectUrl = properties.getRedirectLogin();
        return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT)
                .header(HttpHeaders.LOCATION, KAKAO_AUTH_URL)
                .build();
    }
}