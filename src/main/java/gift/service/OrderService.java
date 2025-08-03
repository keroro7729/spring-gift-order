package gift.service;

import gift.common.dto.response.OrderResponseDto;
import gift.common.exception.BusinessException;
import gift.common.exception.code.ResourceErrorCode;
import gift.domain.member.Member;
import gift.domain.order.Order;
import gift.domain.product.Product;
import gift.domain.product.ProductOption;
import gift.external.kakao.KakaoApiClient;
import gift.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final WishService wishService;
    private final KakaoApiClient kakaoApiClient;

    public OrderService(OrderRepository orderRepository,
                        ProductService productService,
                        WishService wishService,
                        KakaoApiClient kakaoApiClient) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.wishService = wishService;
        this.kakaoApiClient = kakaoApiClient;
    }

    @Transactional
    public OrderResponseDto order(Member member, Long optionId, Integer quantity, String message) {
        Product product = productService.getOptionsOwn(optionId);
        ProductOption option = product.getOptionById(optionId);

        productService.applyOptionSold(optionId, quantity);
        wishService.deleteIfExist(member, product);

        Order order = Order.of(option, quantity, message);
        order = orderRepository.save(order);
        return OrderResponseDto.from(order);
    }

    @Transactional
    public void sendKakaoMessage(Member member, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> BusinessException.of(ResourceErrorCode.ORDER_NOT_FOUND,
                        "존재하지 않는 orderId: " + orderId,
                        HttpStatus.NOT_FOUND));
        String url = URI.create("/api/orders/"+order.getId()).toString();
        kakaoApiClient.sendKakaoMessageToMe(member.getKakaoAccessToken(), order.getPurchaseMessage(), url);
    }

    public OrderResponseDto getOrder(Long orderId) {
        return OrderResponseDto.from(orderRepository.findById(orderId)
                .orElseThrow(() -> BusinessException.of(ResourceErrorCode.ORDER_NOT_FOUND,
                        "존재하지 않는 orderId로 접근",
                        HttpStatus.NOT_FOUND)));
    }
}
