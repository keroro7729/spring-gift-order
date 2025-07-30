package gift.external.kakao.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GetTokenRequestDto(String grantType, String clientId, String redirectUri, String code) {
    public static GetTokenRequestDto of(String clientId, String redirectUri, String code) {
        return new GetTokenRequestDto("authorization_code", clientId, redirectUri, code);
    }

    public MultiValueMap<String, String> toFormData() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", grantType);
        formData.add("client_id", clientId);
        formData.add("redirect_uri", redirectUri);
        formData.add("code", code);
        return formData;
    }
}
