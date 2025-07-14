package gift.common.dto.request;

import gift.domain.product.Product;
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
        String imageUrl
) {
    public Product toEntity() {
        return Product.tempInstance(name, price, imageUrl);
    }
}
