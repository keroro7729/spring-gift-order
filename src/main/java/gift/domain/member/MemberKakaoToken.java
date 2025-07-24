package gift.domain.member;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "member_kakao_token")
public class MemberKakaoToken {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    private Member member;

    @Column(nullable = false)
    private String accessToken;

    @Column(nullable = false)
    private String refreshToken;

    protected MemberKakaoToken() {
    }

    private MemberKakaoToken(Long id, @NotNull String accessToken, @NotNull String refreshToken) {
        this.id = id;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static MemberKakaoToken of(String accessToken, String refreshToken) {
        return new MemberKakaoToken(null, accessToken, refreshToken);
    }

    public Long getId() {
        return id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void refresh(String accessToken) {
        this.accessToken = accessToken;
    }

    public void refresh(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
