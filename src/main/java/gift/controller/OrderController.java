package gift.controller;

import gift.common.annotation.CurrentMember;
import gift.common.dto.request.OrderRequestDto;
import gift.common.dto.response.OrderResponseDto;
import gift.domain.member.Member;
import gift.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping()
    public ResponseEntity<OrderResponseDto> order(@CurrentMember Member member,
                                                  @RequestBody OrderRequestDto request) {
        OrderResponseDto created = orderService.order(member, request.optionId(), request.quantity(), request.message());
        String location = "/api/orders/" + created.id();
        return ResponseEntity.created(URI.create(location)).body(created);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrderDetail(@PathVariable Long orderId) {
        OrderResponseDto response = orderService.get(orderId);
        return ResponseEntity.ok(response);
    }
}
