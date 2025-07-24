package gift.external.kakao.request;

public record RefreshRequestDto(String grant_type, String client_id, String refresh_token) {
    public static RefreshRequestDto of(String clientId, String refreshToken) {
        return new RefreshRequestDto("refresh_token", clientId, refreshToken);
    }
}
