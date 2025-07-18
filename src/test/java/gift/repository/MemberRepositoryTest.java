package gift.repository;

import gift.domain.member.Member;
import gift.domain.member.MemberRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void testFindByEmail() {
        Member saved = memberRepository.save(Member.of(null, "find-me@test.com", "asdf123", MemberRole.USER));

        Member found = memberRepository.findByEmail(saved.getEmail())
                .orElseThrow(() -> new RuntimeException("해당 email의 Member를 찾지 못했습니다: "+saved.getEmail()));

        assertThat(saved.getEmail()).isEqualTo(found.getEmail());
    }
}
