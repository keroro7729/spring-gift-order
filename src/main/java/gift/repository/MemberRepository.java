package gift.repository;

import gift.domain.member.Member;
import gift.domain.member.MemberProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByProviderAndProviderId(MemberProvider provider, String providerId);
}
