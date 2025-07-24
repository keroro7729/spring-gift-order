package gift.external.kakao.response;

public record RefreshResponseDto(String token_type, String access_token, Integer expires_in, String refresh_token, String refresh_token_expires_in) {
}
