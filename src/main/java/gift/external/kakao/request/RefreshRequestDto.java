package gift.external.kakao.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record RefreshRequestDto(String grantType, String clientId, String refreshToken) {
    public static RefreshRequestDto of(String clientId, String refreshToken) {
        return new RefreshRequestDto("refresh_token", clientId, refreshToken);
    }

    public MultiValueMap<String, String> toBodyForm() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", grantType);
        formData.add("client_id", clientId);
        formData.add("refresh_token", refreshToken);
        return formData;
    }
}
