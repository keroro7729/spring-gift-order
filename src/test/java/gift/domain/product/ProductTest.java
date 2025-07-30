package gift.domain.product;

import gift.domain.product.Product;
import gift.domain.product.ProductDomainRuleException;
import gift.domain.product.ProductState;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SoftAssertionsExtension.class)
public class ProductTest {

    @Test
    void 임시생성시_상태는_TEMP() {
        Product product = Product.tempInstance("상품", 100L, null);
        assertThat(product.getState()).isEqualTo(ProductState.TEMP);
    }

    @Test
    void 신규_생성시_옵션이_null이면_ProductDomainRuleException() {
        assertThrows(ProductDomainRuleException.class, () -> {
            Product.create("상품", 100L, null, null);
        }, "상품 생성시 옵션 유효성 검사 실패");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"길이 15 초과 상품 이름11",
            "상품!", "상품@", "상품#", "상품$", "상품%", "상품^", "상품*", "상품=",
            "상품~", "상품`", "상품{", "상품}", "상품\\", "상품|", "상품;", "상품:", "상품?"})
    void 상품이름_유효성검사(String wrongName) {
        assertThrows(ProductDomainRuleException.class, () -> {
            Product.tempInstance(wrongName, 1000L, null);
        }, "상품이름 유효성 검사 실패: " + wrongName);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {-1L, 10000000000L})
    void 상품가격_유효성검사(Long wrongPrice) {
        assertThrows(ProductDomainRuleException.class, () -> {
            Product.tempInstance("상품", wrongPrice, null);
        }, "상품 가격 유효성 검사 실패: "+wrongPrice);
    }

    @Test
    void 상품이름_카카오_포함여부_검사(SoftAssertions softly) {
        Product product = Product.tempInstance("카카오_상품", 1000L, null);
        softly.assertThat(product.isInvolveKakao())
                .as("카카오_상품 검출 실패")
                .isEqualTo(true);

        product = Product.tempInstance("일반_상품", 1000L, null);
        softly.assertThat(product.isInvolveKakao())
                .as("일반_상품 검출 오류")
                .isEqualTo(false);
    }

    @Test
    void addOption(SoftAssertions softly) {
        ProductOption option = ProductOption.of("옵션", 999);
        Product product = Product.create("상품", 1000L, null, option);

        ProductOption newOption = ProductOption.of("새로운옵션", 888);
        product.addOption(newOption);

        softly.assertThat(product.getOptions().contains(newOption)).isEqualTo(true);
        softly.assertThat(product.getOptions().size()).isEqualTo(2);
    }

    @Test
    void removeOption(SoftAssertions softly) {
        Product product = Product.create("상품", 1000L, null, ProductOption.of("test 옵션", 2));
        ProductOption option = ProductOption.of("옵션", 999);
        product.addOption(option);

        product.removeOption(option);

        softly.assertThat(product.getOptions().contains(option)).isEqualTo(false);
        softly.assertThat(product.getOptions().size()).isEqualTo(1);
    }
}
