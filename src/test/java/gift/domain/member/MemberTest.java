package gift.domain.member;

import gift.domain.member.Member;
import gift.domain.member.MemberRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MemberTest {

    @Test
    void 임시생성시_ROLE은_USER() {
        Member member = Member.createTemp("test@test.com", "qwe123!@#");
        assertThat(member.getRole()).isEqualTo(MemberRole.USER);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"email_address", "email!", "email@naver", "email.gmail.com"})
    void 이메일_검증(String email) {
        assertThrows(MemberDomainRuleException.class, () -> {
            Member.createTemp(email, "hashed_password");
        }, "잘못된 이메일 형식이 검증 실패: " + email);
    }
}
