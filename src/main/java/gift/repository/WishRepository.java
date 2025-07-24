package gift.repository;

import gift.domain.member.Member;
import gift.domain.product.Product;
import gift.domain.wish.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface WishRepository extends JpaRepository<Wish, Long> {
    Optional<Wish> findByMemberAndProduct(Member member, Product product);

    Page<Wish> findAll(Pageable pageable);

    Page<Wish> findAllByMember(Pageable pageable, Member member);
}
