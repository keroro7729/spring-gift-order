package gift.domain.product;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SoftAssertionsExtension.class)
public class ProductOptionTest {

    @Test
    void 초기_생성시_id_product_isNull(SoftAssertions softly) {
        ProductOption option = ProductOption.of("option", 1000);
        softly.assertThat(option.getId())
                .as("Option 임시 인스턴스 id가 null이 아님")
                .isNull();
        softly.assertThat(option.getProduct())
                .as("Option 임시 인스턴스 product 필드가 null이 아님")
                .isNull();
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
