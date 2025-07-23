package gift.domain.product;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductOptionTest {

    @Test
    void 초기_생성시_id_product_isNull() {
        ProductOption option = ProductOption.of("option", 1000);
        assertThat(option.getId()).isNull();
        assertThat(option.getProduct()).isNull();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"길이 50 초과 상품 옵션 이름   1234567890123456789012345678901",
            "옵션!", "옵션@", "옵션#", "옵션$", "옵션%", "옵션^", "옵션*", "옵션=",
            "옵션~", "옵션`", "옵션{", "옵션}", "옵션\\", "옵션|", "옵션;", "옵션:", "옵션?"})
    void 잘못된_이름의_옵션_생성시_ProductOptionException(String wrongName) {
        assertThrows(ProductOptionException.class, () -> {
            ProductOption.of(wrongName, 1000);
        }, "옵션 이름 유효성 검사 실패: " + wrongName);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(ints = {0, 100000000, -1})
    void 잘못된_수량의_옵션_생성시_ProductOptionException(Integer wrongQuantity) {
        assertThrows(ProductOptionException.class, () -> {
            ProductOption.of("option", wrongQuantity);
        }, "옵션 수량 유효성 검사 실패: " + wrongQuantity);
    }
}
