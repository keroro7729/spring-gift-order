package gift.external.kakao;

import gift.common.exception.BusinessException;
import gift.common.exception.code.ExternalErrorCode;
import gift.external.kakao.request.GetTokenRequestDto;
import gift.external.kakao.request.RefreshRequestDto;
import gift.external.kakao.response.GetMemberIdResponseDto;
import gift.external.kakao.response.GetTokenResponseDto;
import gift.external.kakao.response.RefreshResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class KakaoApiClient {

    private RestClient client;

    @Value("${kakao.client-id}")
    private String clientId;

    public KakaoApiClient(RestClient.Builder builder) {
        this.client = builder.baseUrl("https://kauth.kakao.com").build();
    }

    public ResponseEntity<GetTokenResponseDto> requestToken(String redirectUri, String code) {
        GetTokenRequestDto request = GetTokenRequestDto.of(clientId, redirectUri, code);
        try {
            return client.post()
                    .uri("/oauth/token")
                    .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                    .body(request)
                    .retrieve()
                    .toEntity(GetTokenResponseDto.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw BusinessException.internal(ExternalErrorCode.KAKAO_OAUTH_TOKEN_FAIL,
                    "KAKAO/oauth/token 요청 실패: "+e.getMessage());
        } catch (RestClientException e) {
            throw BusinessException.internal(ExternalErrorCode.KAKAO_OAUTH_TOKEN_NETWORK_FAIL,
                    "네트워크 오류: "+e.getMessage());
        }
    }

    public ResponseEntity<GetMemberIdResponseDto> requestMemberId(String accessToken) {
        try{
            return client.get()
                    .uri("/v1/user/access_token_info")
                    .header("Authorization", "Bearer "+accessToken)
                    .retrieve()
                    .toEntity(GetMemberIdResponseDto.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw BusinessException.internal(ExternalErrorCode.KAKAO_OAUTH_TOKEN_FAIL,
                    "KAKAO/oauth/token 요청 실패: "+e.getMessage());
        } catch (RestClientException e) {
            throw BusinessException.internal(ExternalErrorCode.KAKAO_OAUTH_TOKEN_NETWORK_FAIL,
                    "네트워크 오류: "+e.getMessage());
        }
    }

    public ResponseEntity<RefreshResponseDto> requestRefresh(String refreshToken) {
        RefreshRequestDto request = RefreshRequestDto.of(clientId, refreshToken);
        try{
            return client.post()
                    .uri("/oauth/token")
                    .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                    .body(request)
                    .retrieve()
                    .toEntity(RefreshResponseDto.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw BusinessException.internal(ExternalErrorCode.KAKAO_OAUTH_TOKEN_FAIL,
                    "KAKAO/oauth/token 요청 실패: "+e.getMessage());
        } catch (RestClientException e) {
            throw BusinessException.internal(ExternalErrorCode.KAKAO_OAUTH_TOKEN_NETWORK_FAIL,
                    "네트워크 오류: "+e.getMessage());
        }
    }
}