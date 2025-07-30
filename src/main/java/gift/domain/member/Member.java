package gift.domain.member;

import jakarta.persistence.*;

@Entity
@Table(name = "member", uniqueConstraints = @UniqueConstraint(columnNames = {"provider", "provider_id"}))
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberProvider provider;

    @Column
    private String providerId;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private MemberKakaoToken kakaoToken;

    protected Member() {
    }

    private Member(Long id, String email, String password, MemberRole role, MemberProvider provider, String providerId, MemberKakaoToken kakaoToken) {
        this.id = id;
        validateEmail(email);
        this.email = email;
        validatePassword(password);
        this.password = password;
        validateRole(role);
        this.role = role;
        validateProvider(provider);
        this.provider = provider;
        this.providerId = providerId;
        this.kakaoToken = kakaoToken;
    }

    public static Member createTemp(String email, String password) {
        return new Member(null, email, password, MemberRole.USER, MemberProvider.LOCAL, null, null);
    }

    public static Member createKakaoInstance(String providerId, MemberKakaoToken kakaoToken) {
        Member created = new Member(null, null, null, MemberRole.USER, MemberProvider.KAKAO, providerId, kakaoToken);
        kakaoToken.setMember(created);
        return created;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public MemberRole getRole() {
        return role;
    }

    public String getRoleName() {
        return role.getRoleName();
    }

    public String getProviderId() {
        return providerId;
    }

    public String getKakaoAccessToken() {
        checkKakaoTokenInstance();
        return kakaoToken.getAccessToken();
    }

    public String getKakaoRefreshToken() {
        checkKakaoTokenInstance();
        return kakaoToken.getRefreshToken();
    }

    public void refresh(String accessToken) {
        kakaoToken.refresh(accessToken);
    }

    public void refresh(String accessToken, String refreshToken) {
        kakaoToken.refresh(accessToken, refreshToken);
    }

    private void validateEmail(String email) {
        if (email == null) return;
        if (email.isBlank()) {
            throw new MemberDomainRuleException("이메일은 필수입니다.");
        }
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new MemberDomainRuleException("이메일 형식이 아닙니다: " + email);
        }
    }

    private void validatePassword(String password) {
        if (password == null) return;
        if (password.isBlank()) {
            throw new MemberDomainRuleException("비밀번호에 null 또는 빈값이 할당됨!!");
        }
    }

    private void validateRole(MemberRole role) {
        if (role == null) {
            throw new MemberDomainRuleException("Member Role이 설정되지 않았습니다. null");
        }
    }

    private void validateProvider(MemberProvider provider) {
        if (provider == null) {
            throw new MemberDomainRuleException("Member Provider가 설정되지 않았습니다. null");
        }
    }

    private void checkKakaoTokenInstance() {
        if (kakaoToken == null) {
            throw new MemberDomainRuleException("카카오 회원가입 사용자가 아님: kakaoToken = null");
        }
    }
}
