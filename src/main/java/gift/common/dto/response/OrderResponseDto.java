package gift.common.dto.response;

import gift.domain.order.Order;

import java.time.LocalDateTime;

public record OrderResponseDto(long id, long optionId, int quantity, LocalDateTime orderDateTime, String message) {
    public static OrderResponseDto from(Order order) {
        return new OrderResponseDto(order.getId(), order.getOptionId(), order.getQuantity(), order.getOrderDateTime(), order.getMessage());
    }
}
