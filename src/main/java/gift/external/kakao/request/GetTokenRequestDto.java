package gift.external.kakao.request;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public record GetTokenRequestDto(String grant_type, String client_id, String redirect_uri, String code) {
    public static GetTokenRequestDto of(String clientId, String redirectUri, String code) {
        return new GetTokenRequestDto("authorization_code", clientId, redirectUri, code);
    }

    public MultiValueMap<String, String> toBodyForm() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", grant_type);
        formData.add("client_id", client_id);
        formData.add("redirect_uri", redirect_uri);
        formData.add("code", code);
        return formData;
    }
}
