package gift.common.dto.response;

import gift.domain.product.Product;

public record ProductResponseDto(Long id, String name, Long price, String imageUrl, String state) {

    public static ProductResponseDto from(Product p) {
        return new ProductResponseDto(p.getId(), p.getName(), p.getPrice(), p.getImageUrl(), p.getStateName());
    }
}
