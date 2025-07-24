package gift.common.dto.request;

import gift.domain.product.ProductOption;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ProductOptionRequestDto(
        @NotBlank(message = "상품 옵션 이름 필수")
        @Pattern(regexp = "^[A-Za-z가-힣0-9()\\[\\]+\\-&/_ ]{1,50}$", message = "영문 한글 숫자 특수기호를 포함한 50자 이내 이름 필요. ()[]+-&/_")
        String optionName,
        @Min(value = 1, message = "옵션 수량 최소값 1 이상의 값 필요")
        @Max(value = 99999999, message = "옵션 수량 1억 미만의 값 필요")
        Integer optionQuantity
) {
    public ProductOption toEntity() {
        return ProductOption.of(optionName, optionQuantity);
    }
}
