package gift.external.kakao.request;

public record GetTokenRequestDto(String grant_type, String client_id, String redirect_uri, String code) {
    public static GetTokenRequestDto of(String clientId, String redirectUri, String code) {
        return new GetTokenRequestDto("authorization_code", clientId, redirectUri, code);
    }
}
