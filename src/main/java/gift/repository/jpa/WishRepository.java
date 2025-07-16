package gift.repository.jpa;

import gift.domain.member.Member;
import gift.domain.product.Product;
import gift.domain.wish.Wish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface WishRepository extends JpaRepository<Wish, Long> {
    Optional<Wish> findByMemberAndProduct(Member member, Product product);
}
