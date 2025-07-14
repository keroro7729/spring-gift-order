package gift.service;

import gift.common.dto.request.ProductRequestDto;
import gift.common.dto.response.MessageResponseDto;
import gift.common.dto.response.ProductResponseDto;
import gift.common.exception.BusinessException;
import gift.common.exception.code.BusinessErrorCode;
import gift.common.exception.code.ResourceErrorCode;
import gift.domain.product.Product;
import gift.domain.product.ProductQueryOption;
import gift.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public MessageResponseDto<ProductResponseDto> create(ProductRequestDto body) {
        Product instance = body.toEntity();
        if (instance.involveKakao()) {
            instance.waitApproval();
            Product created = productRepository.save(instance)
                    .orElseThrow(() -> createProductFail(body.name()));
            return new MessageResponseDto<>(false, "카카오 관련 상품 승인 대기중", 202, ProductResponseDto.from(created));
        }
        instance.onBoard();
        Product created = productRepository.save(instance)
                .orElseThrow(() -> createProductFail(body.name()));
        return new MessageResponseDto<>(true, "상품 생성 완료", 201, ProductResponseDto.from(created));
    }

    public ProductResponseDto get(Long id, ProductQueryOption option) {
        Product result = find(id);
        if (!result.isShowable(option)) {
            throw BusinessException.of(
                    BusinessErrorCode.PRODUCT_NOT_SELLING,
                    "판매하지 않는 상품에 접근하셨습니다.",
                    HttpStatus.BAD_REQUEST
            );
        }
        return ProductResponseDto.from(result);
    }

    public List<ProductResponseDto> getList(ProductQueryOption option) {
        return productRepository.findAll().stream()
                .filter(p -> p.isShowable(option))
                .sorted(Comparator.comparing(Product::getId))
                .map(ProductResponseDto::from)
                .toList();
    }

    public MessageResponseDto<ProductResponseDto> update(Long id, ProductRequestDto body) {
        find(id);
        Product instance = body.toEntity();
        if (instance.involveKakao()) {
            instance.waitApproval();
            Product updated = productRepository.update(id, instance).get();
            return new MessageResponseDto<>(false, "카카오 관련 상품 승인 대기중", 202, ProductResponseDto.from(updated));
        }
        instance.onBoard();
        Product updated = productRepository.update(id, instance).get();
        return new MessageResponseDto<>(true, "상품 수정 완료", 200, ProductResponseDto.from(updated));
    }

    public void delete(Long id) {
        find(id);
        productRepository.delete(id);
    }

    private Product find(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new BusinessException.Builder(ResourceErrorCode.PRODUCT_NOT_FOUND, "Product id: " + id)
                        .clientMessage("존재하지 않는 상품에 접근")
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .logLevel(2)
                        .build()
                );
    }

    private BusinessException createProductFail(String name) {
        return BusinessException.internal(
                ResourceErrorCode.PRODUCT_NOT_FOUND,
                String.format("Fail to create Product(%s)", name)
        );
    }
}
