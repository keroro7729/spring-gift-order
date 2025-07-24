package gift.controller;
import gift.external.kakao.response.GetMemberIdResponseDto;
import gift.external.kakao.response.GetTokenResponseDto;
import gift.external.kakao.KakaoApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/oauth/kakao")
public class KakaoOauthController {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-register}")
    private String redirectRegister;

    @Value("${kakao.redirect-login}")
    private String redirectLogin;

    private final KakaoApiClient kakaoClient;

    public KakaoOauthController(KakaoApiClient kakaoClient) {
        this.kakaoClient = kakaoClient;
    }

    @GetMapping("/register/code")
    public ResponseEntity<Void> register() {
        return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT)
                .header("Location", getKakaoAuthUrl(redirectRegister))
                .build();
    }

    @GetMapping("/login/code")
    public ResponseEntity<Void> login() {
        return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT)
                .header("Location", getKakaoAuthUrl(redirectLogin))
                .build();
    }

    @GetMapping("/register")
    public ResponseEntity<Void> authorization(@RequestParam String code) {
        ResponseEntity<GetTokenResponseDto> tokenResponse = kakaoClient.requestToken(redirectRegister, code);
        ResponseEntity<GetMemberIdResponseDto> idResponse = kakaoClient.requestMemberId(tokenResponse.getBody().access_token());
        // 맴버 생성 및 access-token, refresh-token 저장
        // 사용자 정보로 jwt 발급
        return null;
    }

    private String getKakaoAuthUrl(String redirectUri) {
        return "https://kauth.kakao.com/oauth/authorize?"+
                "client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code";
    }
}
