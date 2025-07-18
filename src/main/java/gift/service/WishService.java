package gift.service;

import gift.common.dto.request.AddWishRequestDto;
import gift.common.dto.response.WishResponseDto;
import gift.common.exception.BusinessException;
import gift.common.exception.code.ResourceErrorCode;
import gift.common.exception.code.SecurityErrorCode;
import gift.domain.member.Member;
import gift.domain.product.Product;
import gift.domain.wish.Wish;
import gift.repository.WishRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final MemberService memberService;
    private final ProductService productService;

    public WishService(WishRepository wishRepository,
                       MemberService memberService,
                       ProductService productService) {
        this.wishRepository = wishRepository;
        this.memberService = memberService;
        this.productService = productService;
    }

    @Transactional
    public WishResponseDto add(Member member, AddWishRequestDto request) {
        Product product = productService.getById(request.productId());
        Optional<Wish> found = wishRepository.findByMemberAndProduct(member, product);
        Wish wish;
        if (found.isEmpty()) {
            wish = create(member.getId(), request.productId(), request.quantity());
        } else {
            wish = increaseQuantity(found.get(), request.quantity());
        }
        return WishResponseDto.from(wish);
    }

    public List<WishResponseDto> getOwnList(Pageable pageable, Member member) {
        return wishRepository.findAllByMember(pageable, member).stream()
                .map(WishResponseDto::from)
                .toList();
    }

    @Transactional
    public void delete(Member member, Long wishId) {
        Wish wish = wishRepository.findById(wishId)
                .orElseThrow(() -> BusinessException.of(
                        ResourceErrorCode.WISH_NOT_FOUND,
                        "Wish does not exist: id = " + wishId,
                        HttpStatus.NOT_FOUND
                ));
        if (!wish.isOwner(member.getId())) {
            throw BusinessException.of(
                    SecurityErrorCode.AUTH_FORBIDDEN,
                    "해당 Wish에 접근할 권한이 없습니다.",
                    HttpStatus.FORBIDDEN
            );
        }
        wishRepository.delete(wish);
    }

    private Wish create(Long memberId, Long productId, Integer quantity) {
        Member member = memberService.getById(memberId);
        Product product = productService.getById(productId);
        Wish instance = Wish.of(null, member, product, quantity);
        return wishRepository.save(instance);
    }

    private Wish increaseQuantity(Wish wish, Integer addQuantity) {
        wish.addQuantity(addQuantity);
        return wish;
    }
}
