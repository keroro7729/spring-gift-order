package gift.service;

import gift.common.dto.request.MemberRequestDto;
import gift.common.dto.response.TokenResponseDto;
import gift.common.exception.BusinessException;
import gift.common.exception.EntityNotFoundException;
import gift.common.exception.code.BusinessErrorCode;
import gift.common.exception.code.ResourceErrorCode;
import gift.common.exception.code.SecurityErrorCode;
import gift.domain.member.Member;
import gift.domain.member.MemberKakaoToken;
import gift.domain.member.MemberProvider;
import gift.repository.MemberRepository;
import gift.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public MemberService(MemberRepository memberRepository,
                         JwtUtil jwt) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwt;
    }

    @Transactional
    public TokenResponseDto handleRegisterRequest(MemberRequestDto request) {
        register(request.email(), request.password());
        return login(request.email(), request.password());
    }

    @Transactional
    public TokenResponseDto handleLoginRequest(MemberRequestDto request) {
        return login(request.email(), request.password());
    }

    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Member not found, email: " + email));
    }

    public Member getById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Member not found, id: " + id));
    }

    @Transactional
    public TokenResponseDto kakaoRegister(Long providerId, String accessToken, String refreshToken) {
        String id = providerId.toString();
        if (memberRepository.findByProviderAndProviderId(MemberProvider.KAKAO, id).isPresent()) {
            throw BusinessException.of(BusinessErrorCode.REGISTER_KAKAO_ID_CONFLICT,
                    "이미 가입된 계정입니다.",
                    HttpStatus.CONFLICT);
        }

        MemberKakaoToken kakaoToken = MemberKakaoToken.of(accessToken, refreshToken);
        Member instance = Member.createKakaoInstance(id, kakaoToken);
        Member saved = memberRepository.save(instance);
        String token = jwtUtil.createToken(saved.getProviderId(), MemberProvider.KAKAO);
        return new TokenResponseDto(token);
    }

    @Transactional
    public TokenResponseDto kakaoLogin(Long providerId, String accessToken, String refreshToken) {
        String id = providerId.toString();
        Member member = memberRepository.findByProviderAndProviderId(MemberProvider.KAKAO, id)
                .orElseThrow(() -> BusinessException.of(SecurityErrorCode.NOT_REGISTERED_KAKAO_MEMBER,
                        "가입되지 않은 카카오 계정입니다.",
                        HttpStatus.BAD_REQUEST));
        member.refresh(accessToken, refreshToken);
        String token = jwtUtil.createToken(member.getProviderId(), MemberProvider.KAKAO);
        return new TokenResponseDto(token);
    }

    public boolean isKakaoMemberExist(Long providerId) {
        return memberRepository.findByProviderAndProviderId(MemberProvider.KAKAO, providerId.toString()).isPresent();
    }

    public Member getByKakaoId(String kakaoId) {
        return memberRepository.findByProviderAndProviderId(MemberProvider.KAKAO, kakaoId)
                .orElseThrow(() -> BusinessException.of(ResourceErrorCode.MEMBER_NOT_FOUND,
                        "해당 카카오 식별자의 맴버를 찾을 수 없습니다.",
                        HttpStatus.NOT_FOUND));
    }

    private Member register(String email, String plainPassword) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new BusinessException.Builder(BusinessErrorCode.REGISTER_EMAIL_CONFLICT, "Register email conflict: email=" + email)
                    .clientMessage("이미 등록된 이메일 입니다.")
                    .httpStatus(HttpStatus.CONFLICT)
                    .logLevel(2)
                    .build();
        }
        String encryptedPassword = encoder.encode(plainPassword);
        Member instance = Member.createTemp(email, encryptedPassword);
        return memberRepository.save(instance);
    }

    private TokenResponseDto login(String email, String plainPassword) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> BusinessException.of(
                                SecurityErrorCode.LOGIN_EMAIL_NOT_FOUND,
                                "등록되지 않은 이메일",
                                HttpStatus.UNAUTHORIZED
                        )
                );
        if (!encoder.matches(plainPassword, member.getPassword())) {
            throw BusinessException.of(
                    SecurityErrorCode.AUTH_INVALID_TOKEN,
                    "잘못된 비밀번호",
                    HttpStatus.UNAUTHORIZED
            );
        }
        String token = jwtUtil.createToken(member.getEmail(), MemberProvider.LOCAL);
        return new TokenResponseDto(token);
    }
}
