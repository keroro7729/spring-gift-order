package gift.domain.product;

import gift.domain.product.Product;
import gift.domain.product.ProductDomainRuleException;
import gift.domain.product.ProductState;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductTest {

    @Test
    void 임시생성시_상태는_TEMP() {
        Product product = Product.tempInstance("상품", 100L, null);
        assertThat(product.getState()).isEqualTo(ProductState.TEMP);
    }

    @Test
    void 상품이름_유효성검사() {
        String[] wrongNames = {
                null, "", "길이 15 초과 상품 이름11",
                "상품!", "상품@", "상품#", "상품$", "상품%", "상품^", "상품*", "상품=",
                "상품~", "상품`", "상품{", "상품}", "상품\\", "상품|", "상품;", "상품:", "상품?"
        };
        for (String wrongName : wrongNames) {
            assertThrows(ProductDomainRuleException.class, () -> {
                Product.tempInstance(wrongName, 1000L, null);
            }, "상품이름 유효성 검사 실패: " + wrongName);
        }
    }

    @Test
    void 상품가격_유효성검사() {
        Long[] wrongPrices = {
                null, -1L, 10000000000L
        };
        for (Long wrongPrice : wrongPrices) {
            assertThrows(ProductDomainRuleException.class, () -> {
                Product.tempInstance("상품", wrongPrice, null);
            }, "상품 가격 유효성 검사 실패: "+wrongPrice);
        }
    }

    @Test
    void 상품이름_카카오_포함여부_검사() {
        Product product = Product.tempInstance("카카오_상품", 1000L, null);
        assertThat(product.isInvolveKakao()).isEqualTo(true);

        product = Product.tempInstance("일반_상품", 1000L, null);
        assertThat(product.isInvolveKakao()).isEqualTo(false);
    }
}
