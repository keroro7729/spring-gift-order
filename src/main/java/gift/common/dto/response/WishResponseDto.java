package gift.common.dto.response;

import gift.domain.wish.Wish;

public record WishResponseDto(Long id, Long productId, Integer quantity) {
    public static WishResponseDto from(Wish wish) {
        return new WishResponseDto(wish.getId(), wish.getProductId(), wish.getQuantity());
    }
}
