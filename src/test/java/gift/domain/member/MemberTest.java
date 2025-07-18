package gift.domain.member;

import gift.domain.member.Member;
import gift.domain.member.MemberRole;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberTest {

    @Test
    void 임시생성시_ROLE은_USER() {
        Member member = Member.createTemp("test@test.com", "qwe123!@#");
        assertThat(member.getRole()).isEqualTo(MemberRole.USER);
    }

    // TODO: 도메인 검증 추가 필요
}
