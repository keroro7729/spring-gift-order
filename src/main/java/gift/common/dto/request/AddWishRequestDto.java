package gift.common.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AddWishRequestDto(
        @NotNull(message = "Require productId!") Long productId,
        @Positive(message = "Quantity should be positive!") Integer quantity) {

    public AddWishRequestDto {
        if (quantity == null) {
            quantity = 1;
        }
    }
}
