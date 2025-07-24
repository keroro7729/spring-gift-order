package gift.common.dto.request;

import gift.domain.product.Product;
import gift.domain.product.ProductOption;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ProductRequestDto(
        @NotBlank(message = "Product name is required")
        @Pattern(regexp = "^[A-Za-z가-힣0-9()\\[\\]+\\-&/_ ]{1,15}$", message = "영문, 한글, 숫자, ()[]+-&/_ 를 사용한 15이하 이름만 허용됨")
        String name,
        @Min(value = 0, message = "Product price must be positive")
        // Product.MAX_PRICE
        @Max(value = 9999999999L, message = "Product price is limit to 10 digits")
        Long price,
        String imageUrl,
        @NotBlank(message = "상품 옵션 이름 필수")
        @Pattern(regexp = "^[A-Za-z가-힣0-9()\\[\\]+\\-&/_ ]{1,50}$", message = "영문 한글 숫자 특수기호를 포함한 50자 이내 이름 필요. ()[]+-&/_")
        String optionName,
        @Min(value = 1, message = "옵션 수량 최소값 1 이상의 값 필요")
        @Max(value = 99999999, message = "옵션 수량 1억 미만의 값 필요")
        Integer optionQuantity
) {
    public Product toEntity() {
        return Product.create(name, price, imageUrl, ProductOption.of(optionName, optionQuantity));
    }
}
