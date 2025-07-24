package gift.common.dto.response;

import gift.domain.product.ProductOption;

public record ProductOptionResponseDto(
        Long id,
        String name,
        Integer quantity
) {
    public static ProductOptionResponseDto from(ProductOption option) {
        return new ProductOptionResponseDto(option.getId(), option.getName(), option.getQuantity());
    }
}
