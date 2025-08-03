package gift.controller;
import gift.common.dto.response.TokenResponseDto;
import gift.external.kakao.response.GetMemberIdResponseDto;
import gift.external.kakao.response.GetTokenResponseDto;
import gift.external.kakao.KakaoApiClient;
import gift.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/oauth/kakao")
public class KakaoOauthController {

    private final KakaoApiClient kakaoClient;

    private final MemberService memberService;

    public KakaoOauthController(KakaoApiClient kakaoClient, MemberService memberService) {
        this.kakaoClient = kakaoClient;
        this.memberService = memberService;
    }

    @GetMapping("/login/auth")
    public ResponseEntity<Void> loginCode() {
        return kakaoClient.redirectToKakaoAuth();
    }

    @GetMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestParam String code) {
        GetTokenResponseDto tokenResponse = kakaoClient.requestToken(code);
        GetMemberIdResponseDto idResponse = kakaoClient.requestMemberId(tokenResponse.accessToken());

        if(memberService.isKakaoMemberExist(idResponse.id())) {
            TokenResponseDto response = memberService.kakaoLogin(idResponse.id(), tokenResponse.accessToken(), tokenResponse.refreshToken());
            return ResponseEntity.ok(response);
        }
        TokenResponseDto response = memberService.kakaoRegister(idResponse.id(), tokenResponse.accessToken(), tokenResponse.refreshToken());
        return ResponseEntity.created(URI.create("")).body(response);
    }


}
