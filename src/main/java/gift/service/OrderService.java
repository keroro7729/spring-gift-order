package gift.service;

import gift.common.dto.response.OrderResponseDto;
import gift.domain.member.Member;
import gift.domain.order.Order;
import gift.domain.product.Product;
import gift.domain.product.ProductOption;
import gift.external.kakao.KakaoApiClient;
import gift.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberService memberService;
    private final ProductService productService;
    private final WishService wishService;
    private final KakaoApiClient kakaoApiClient;

    public OrderService(OrderRepository orderRepository,
                        MemberService memberService,
                        ProductService productService,
                        WishService wishService,
                        KakaoApiClient kakaoApiClient) {
        this.orderRepository = orderRepository;
        this.memberService = memberService;
        this.productService = productService;
        this.wishService = wishService;
        this.kakaoApiClient = kakaoApiClient;
    }

    @Transactional
    public OrderResponseDto order(Member member, Long optionId, Integer quantity, String message) {
        Product product = productService.getOptionsOwn(optionId);
        ProductOption option = product.getOptionById(optionId);

        productService.applyOptionSold(optionId, quantity);
        wishService.consume(member, product);

        Order order = Order.of(optionId, quantity, message);
        order = orderRepository.save(order);

        String accessToken = member.getKakaoAccessToken();
        String url = "http://localhost:8080/api/orders/" + order.getId();
        kakaoApiClient.sendKakaoMessageToMe(accessToken, purchaseMessage(product.getName(), option.getName()), url);
        return OrderResponseDto.from(order);
    }

    private String purchaseMessage(String productName, String optionName) {
        return String.format("고객님께서 구매하신 상품 %s: %s이 주문 완료되었습니다.", productName, optionName);
    }
}
