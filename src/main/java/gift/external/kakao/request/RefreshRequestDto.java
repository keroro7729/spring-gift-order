package gift.external.kakao.request;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


public record RefreshRequestDto(String grant_type, String client_id, String refresh_token) {
    public static RefreshRequestDto of(String clientId, String refreshToken) {
        return new RefreshRequestDto("refresh_token", clientId, refreshToken);
    }

    public MultiValueMap<String, String> toBodyForm() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", grant_type);
        formData.add("client_id", client_id);
        formData.add("refresh_token", refresh_token);
        return formData;
    }
}
