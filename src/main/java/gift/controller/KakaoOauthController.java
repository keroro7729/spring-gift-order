package gift.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/oauth/kakao")
public class KakaoOauthController {

    private static final Logger log = LoggerFactory.getLogger(KakaoOauthController.class);
    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @GetMapping("/register")
    public ResponseEntity<Void> register() {
        return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT)
                .header("Location", getKakaoAuthUrl())
                .build();
    }

    @GetMapping("/login")
    public ResponseEntity<Void> login() {
        return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT)
                .header("Location", getKakaoAuthUrl())
                .build();
    }

    @GetMapping("/auth-code")
    public ResponseEntity<Void> authorization(@RequestParam String code) {
        // 카카오로 토큰 요청
        // 응답 토큰으로 사용자 정보 요청
        // 사용자 정보로 jwt 발급
        return null;
    }

    private String getKakaoAuthUrl() {
        return "https://kauth.kakao.com/oauth/authorize?"+
                "client_id=" + kakaoClientId +
                "&redirect_uri=" + kakaoRedirectUri +
                "&response_type=code";
    }
}
