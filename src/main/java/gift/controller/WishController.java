package gift.controller;

import gift.common.annotation.CurrentMember;
import gift.common.dto.request.AddWishRequestDto;
import gift.common.dto.response.WishResponseDto;
import gift.domain.member.Member;
import gift.service.WishService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishes")
public class WishController {
    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @PostMapping("/add")
    public ResponseEntity<WishResponseDto> addWish(@CurrentMember Member member,
                                                   @RequestBody @Valid AddWishRequestDto request) {
        WishResponseDto response = wishService.add(member, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<WishResponseDto>> getMyWishList(@CurrentMember Member member) {
        List<WishResponseDto> response = wishService.getOwnList(member);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{wishId}")
    public ResponseEntity<Void> deleteWish(@CurrentMember Member member,
                                           @PathVariable Long wishId) {
        wishService.delete(member, wishId);
        return ResponseEntity.noContent().build();
    }
}
