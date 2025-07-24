package gift.controller;
import gift.common.dto.response.TokenResponseDto;
import gift.external.kakao.response.GetMemberIdResponseDto;
import gift.external.kakao.response.GetTokenResponseDto;
import gift.external.kakao.KakaoApiClient;
import gift.service.MemberService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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

    private final MemberService memberService;

    public KakaoOauthController(KakaoApiClient kakaoClient, MemberService memberService) {
        this.kakaoClient = kakaoClient;
        this.memberService = memberService;
    }

    @GetMapping("/register/code")
    public ResponseEntity<Void> registerCode() {
        return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT)
                .header("Location", getKakaoAuthUrl(redirectRegister))
                .build();
    }

    @GetMapping("/login/code")
    public ResponseEntity<Void> loginCode() {
        return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT)
                .header("Location", getKakaoAuthUrl(redirectLogin))
                .build();
    }

    @GetMapping("/register")
    public ResponseEntity<TokenResponseDto> register(@RequestParam String code) {
        GetTokenResponseDto tokenResponse = kakaoClient.requestToken(redirectRegister, code).getBody();
        GetMemberIdResponseDto idResponse = kakaoClient.requestMemberId(tokenResponse.access_token()).getBody();
        TokenResponseDto response = memberService.kakaoRegister(idResponse.id(), tokenResponse.access_token(), tokenResponse.refresh_token());
        return ResponseEntity.created(URI.create("")).body(response);
    }

    @GetMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestParam String code) {
        GetTokenResponseDto tokenResponse = kakaoClient.requestToken(redirectLogin, code).getBody();
        GetMemberIdResponseDto idResponse = kakaoClient.requestMemberId(tokenResponse.access_token()).getBody();
        TokenResponseDto response = memberService.kakaoLogin(idResponse.id(), tokenResponse.access_token(), tokenResponse.refresh_token());
        return ResponseEntity.ok(response);
    }

    private String getKakaoAuthUrl(String redirectUri) {
        return "https://kauth.kakao.com/oauth/authorize?"+
                "client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code";
    }
}
