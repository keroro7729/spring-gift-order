package gift.controller;

import gift.common.dto.request.ProductOptionRequestDto;
import gift.common.dto.request.ProductRequestDto;
import gift.common.dto.request.ProductUpdateRequestDto;
import gift.common.dto.response.MessageResponseDto;
import gift.common.dto.response.ProductOptionResponseDto;
import gift.common.dto.response.ProductResponseDto;
import gift.common.exception.BusinessException;
import gift.common.exception.code.BusinessErrorCode;
import gift.domain.product.ProductQueryOption;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<MessageResponseDto<ProductResponseDto>> createProduct(@Valid @RequestBody ProductRequestDto body) {
        MessageResponseDto<ProductResponseDto> response = productService.create(body);
        if (response.success()) {
            URI location = URI.create("/api/products/" + response.data().id());
            return ResponseEntity.created(location).body(response);
        }
        return ResponseEntity.accepted().body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable Long id,
                                                         @RequestParam(defaultValue = "SELLING") ProductQueryOption option) {
        ProductResponseDto response = productService.get(id, option);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProduct(@RequestParam(defaultValue = "SELLING") ProductQueryOption option,
                                                                  @PageableDefault(sort = "id") Pageable pageable) {
        List<ProductResponseDto> response;
        switch (option) {
            case ALL -> response = productService.getAll(pageable);
            case SELLING -> response = productService.getSelling(pageable);
            default -> throw BusinessException.of(
                    BusinessErrorCode.UNKNOWN_PRODUCT_QUERY_OPTION,
                    "Unknown product query option: " + option.name(),
                    HttpStatus.BAD_REQUEST
            );
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponseDto<ProductResponseDto>> updateProduct(@PathVariable Long id,
                                                                                @Valid @RequestBody ProductUpdateRequestDto body) {
        MessageResponseDto<ProductResponseDto> response = productService.update(id, body);
        if (response.success()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.accepted().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{productId}/options")
    public ResponseEntity<ProductOptionResponseDto> addOptionTo(@PathVariable Long productId,
                                                                @Valid @RequestBody ProductOptionRequestDto body) {
        ProductOptionResponseDto created = ProductOptionResponseDto.from(productService.addOptionTo(productId, body));
        String location = "/api/products/" + productId + "/options";
        return ResponseEntity.created(URI.create(location)).body(created);
    }

    @GetMapping("/{productId}/options")
    public ResponseEntity<List<ProductOptionResponseDto>> getOptionsOf(@PathVariable Long productId) {
        List<ProductOptionResponseDto> response = productService.getOptionsOf(productId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{productId}/options/{optionId}")
    public ResponseEntity<Void> deleteOptionOf(@PathVariable Long productId,
                                               @PathVariable Long optionId) {
        productService.deleteOptionOf(productId, optionId);
        return ResponseEntity.noContent().build();
    }
}
