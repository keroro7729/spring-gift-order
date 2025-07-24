package gift.repository;

import gift.domain.product.Product;
import gift.domain.product.ProductState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testFindAllByState() {
        Product p1 = Product.of(null, "판매중인상품", 1000L, null, ProductState.SELLING);
        Product p2 = Product.of(null, "승인대기중인상품", 1000L, null, ProductState.WAITING);
        entityManager.persist(p1);
        entityManager.persist(p2);
        entityManager.flush();
        entityManager.clear();

        Pageable pageable = PageRequest.of(0, 10);
        productRepository.findAllByState(pageable, ProductState.SELLING).stream()
                .forEach(p -> assertThat(p.getState()).isEqualTo(ProductState.SELLING));
        productRepository.findAllByState(pageable, ProductState.WAITING).stream()
                .forEach(p -> assertThat(p.getState()).isEqualTo(ProductState.WAITING));
    }
}
