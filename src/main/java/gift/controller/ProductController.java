package gift.controller;

import gift.common.dto.request.ProductRequestDto;
import gift.common.dto.response.MessageResponseDto;
import gift.common.dto.response.ProductResponseDto;
import gift.domain.product.ProductQueryOption;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
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
                                                                  Pageable pageable) {
        List<ProductResponseDto> response = productService.getList(pageable, option);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponseDto<ProductResponseDto>> updateProduct(@PathVariable Long id,
                                                                                @RequestBody ProductRequestDto body) {
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
}
