package gift.external.kakao;

import gift.common.exception.BusinessException;
import gift.common.exception.code.ExternalErrorCode;
import gift.config.KakaoProperties;
import gift.external.kakao.request.GetTokenRequestDto;
import gift.external.kakao.request.RefreshRequestDto;
import gift.external.kakao.response.GetMemberIdResponseDto;
import gift.external.kakao.response.GetTokenResponseDto;
import gift.external.kakao.response.RefreshResponseDto;
import gift.external.kakao.response.ResultCodeResponseDto;
import gift.external.kakao.template.TemplateObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class KakaoApiClient {

    private static final Logger log = LoggerFactory.getLogger(KakaoApiClient.class);
    private final KakaoProperties properties;
    private final RestClient kauthClient;
    private final RestClient kapiClient;

    public KakaoApiClient(@Qualifier("kauthClient") RestClient kauthClient,
                          @Qualifier("kapiClient") RestClient kapiClient,
                          KakaoProperties properties) {
        this.kauthClient = kauthClient;
        this.kapiClient = kapiClient;
        this.properties = properties;
    }

    public GetTokenResponseDto requestToken(String code) {
        GetTokenRequestDto request = GetTokenRequestDto.of(properties.getClientId(), properties.getRedirectLogin(), code);
        try {
            return kauthClient.post()
                    .uri("/oauth/token")
                    .body(request.toFormData())
                    .retrieve()
                    .toEntity(GetTokenResponseDto.class)
                    .getBody();
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

    public GetMemberIdResponseDto requestMemberId(String accessToken) {
        try {
            return kapiClient.get()
                    .uri("/v1/user/access_token_info")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .toEntity(GetMemberIdResponseDto.class)
                    .getBody();
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

    public ResultCodeResponseDto sendKakaoMessageToMe(String accessToken, String text, String url) {
        TemplateObject request = TemplateObject.of(text, url);
        try{
            return kapiClient.post()
                    .uri("/v2/api/talk/memo/default/send")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8")
                    .body(request.toFormData())
                    .retrieve()
                    .toEntity(ResultCodeResponseDto.class)
                    .getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("KAKAO/v2/api/talk/memo/default/send fail detail: ", e);
            throw BusinessException.internal(ExternalErrorCode.KAKAO_OAUTH_TOKEN_FAIL,
                    "KAKAOv2/api/talk/memo/default/send 요청 실패: " + e.getMessage());
        } catch (RestClientException e) {
            log.error("KAKAOv2/api/talk/memo/default/send fail detail: ", e);
            throw BusinessException.internal(ExternalErrorCode.KAKAO_OAUTH_TOKEN_NETWORK_FAIL,
                    "네트워크 오류: " + e.getMessage());
        }
    }

    public ResponseEntity<Void> redirectToKakaoAuth() {
        String redirectUrl = properties.getRedirectLogin();
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, getKakaoAuthUrl())
                .build();
    }

    private String getKakaoAuthUrl() {
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("kauth.kakao.com")
                .path("/oauth/authorize")
                .queryParam("client_id", properties.getClientId())
                .queryParam("redirect_uri", properties.getRedirectLogin())
                .queryParam("response_type", "code")
                .build().toUriString();
    }
}