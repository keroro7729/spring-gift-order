package gift.service;

import gift.common.dto.request.AddWishRequestDto;
import gift.common.dto.response.WishResponseDto;
import gift.common.exception.BusinessException;
import gift.common.exception.code.DatabaseErrorCode;
import gift.common.exception.code.ResourceErrorCode;
import gift.common.exception.code.SecurityErrorCode;
import gift.domain.member.Member;
import gift.domain.wish.Wish;
import gift.repository.WishRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishService {

    private final WishRepository wishRepository;

    public WishService(WishRepository wishRepository) {
        this.wishRepository = wishRepository;
    }

    public WishResponseDto add(Member member, AddWishRequestDto request) {
        Optional<Wish> found = wishRepository.findByMemberIdProductId(member.getId(), request.productId());
        Wish wish;
        if (found.isEmpty()) {
            wish = create(member.getId(), request.productId(), request.quantity());
        } else {
            wish = increaseQuantity(found.get(), request.quantity());
        }
        return WishResponseDto.from(wish);
    }

    public List<WishResponseDto> getOwnList(Member member) {
        return wishRepository.findAll().stream()
                .filter(w -> w.getMemberId().equals(member.getId()))
                .map(WishResponseDto::from)
                .toList();
    }

    public void delete(Member member, Long wishId) {
        Wish wish = wishRepository.findById(wishId)
                .orElseThrow(() -> BusinessException.of(
                        ResourceErrorCode.WISH_NOT_FOUND,
                        "Wish does not exist: id = " + wishId,
                        HttpStatus.NOT_FOUND
                ));
        if (!wish.getMemberId().equals(member.getId())) {
            throw BusinessException.of(
                    SecurityErrorCode.AUTH_FORBIDDEN,
                    "해당 상품에 접근할 권한이 없습니다.",
                    HttpStatus.FORBIDDEN
            );
        }
        wishRepository.delete(wishId);
    }

    private Wish create(Long memberId, Long productId, Integer quantity) {
        Wish instance = Wish.of(null, memberId, productId, quantity);
        return wishRepository.save(instance)
                .orElseThrow(() -> BusinessException.internal(
                        DatabaseErrorCode.WISH_CREATION_FAIL,
                        String.format("Fail to create Wish(member=%d, product=%d, quantity=%d)", memberId, productId, quantity)
                ));
    }

    private Wish increaseQuantity(Wish wish, Integer addQuantity) {
        wish.addQuantity(addQuantity);
        return wishRepository.update(wish.getId(), wish)
                .orElseThrow(() -> BusinessException.of(
                        ResourceErrorCode.WISH_NOT_FOUND,
                        "Wish does not exist: id = " + wish.getId(),
                        HttpStatus.NOT_FOUND)
                );
    }
}
