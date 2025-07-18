package gift.repository;

import gift.domain.member.Member;
import gift.domain.member.MemberRole;
import gift.domain.product.Product;
import gift.domain.product.ProductState;
import gift.domain.wish.Wish;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class WishRepositoryTest {

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testFindByMemberAndProduct() {
        Member member = Member.of(null, "test@test.com", "asdf1234", MemberRole.USER);
        Product product = Product.of(null, "상품", 1000L, null, ProductState.TEMP);
        entityManager.persist(member);
        entityManager.persist(product);

        Wish wish = Wish.of(null, member, product, 10);
        entityManager.persist(wish);
        entityManager.flush();
        entityManager.clear();

        Wish found = wishRepository.findByMemberAndProduct(member, product)
                .orElseThrow(() -> new RuntimeException("testFindByMemberAndProduct() failed!"));

        assertThat(found.getMember().getEmail()).isEqualTo(member.getEmail());
        assertThat(found.getProduct().getName()).isEqualTo(product.getName());
    }

    @Test
    void testFindAllByMember() {
        Member member = Member.of(null, "test@test.com", "asdf1234", MemberRole.USER);
        Product product = Product.of(null, "상품", 1000L, null, ProductState.TEMP);
        entityManager.persist(member);
        entityManager.persist(product);

        Wish wish = Wish.of(null, member, product, 10);
        entityManager.persist(wish);
        entityManager.flush();
        entityManager.clear();

        Pageable pageable = PageRequest.of(0, 10);
        Page<Wish> founds = wishRepository.findAllByMember(pageable, member);

        founds.stream()
                .forEach(w -> assertThat(w.getMember().getEmail()).isEqualTo(member.getEmail()));
        System.out.println(" Result: " + founds);
    }
}
