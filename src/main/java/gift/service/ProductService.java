package gift.service;

import gift.common.dto.request.ProductRequestDto;
import gift.common.dto.response.MessageResponseDto;
import gift.common.dto.response.ProductResponseDto;
import gift.common.exception.BusinessException;
import gift.common.exception.code.BusinessErrorCode;
import gift.common.exception.code.ResourceErrorCode;
import gift.domain.product.Product;
import gift.domain.product.ProductQueryOption;
import gift.domain.product.ProductState;
import gift.repository.ProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public MessageResponseDto<ProductResponseDto> create(ProductRequestDto body) {
        Product instance = body.toEntity();
        if (instance.isInvolveKakao()) {
            instance.waitApproval();
            Product created = productRepository.save(instance);
            return new MessageResponseDto<>(false, "카카오 관련 상품 승인 대기중", 202, ProductResponseDto.from(created));
        }
        instance.onBoard();
        Product created = productRepository.save(instance);
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

    public List<ProductResponseDto> getList(Pageable pageable, ProductQueryOption option) {
        switch (option) {
            case ALL -> {
                return productRepository.findAll(pageable).stream()
                    .map(ProductResponseDto::from)
                    .toList();
            }
            case SELLING -> {
                return productRepository.findAllByState(pageable, ProductState.SELLING).stream()
                        .map(ProductResponseDto::from)
                        .toList();
            }
            default -> throw BusinessException.of(
                    BusinessErrorCode.UNKNOWN_PRODUCT_QUERY_OPTION,
                    "Unknown product query option: " + option.name(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @Transactional
    public MessageResponseDto<ProductResponseDto> update(Long id, ProductRequestDto body) {
        find(id);
        Product instance = body.toEntity();
        instance.setId(id);
        if (instance.isInvolveKakao()) {
            instance.waitApproval();
            Product updated = productRepository.save(instance);
            return new MessageResponseDto<>(false, "카카오 관련 상품 승인 대기중", 202, ProductResponseDto.from(updated));
        }
        instance.onBoard();
        Product updated = productRepository.save(instance);
        return new MessageResponseDto<>(true, "상품 수정 완료", 200, ProductResponseDto.from(updated));
    }

    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> BusinessException.of(
                                ResourceErrorCode.PRODUCT_NOT_FOUND,
                                "상품이 존재하지 않습니다.",
                                HttpStatus.NOT_FOUND
                        )
                );
    }

    @Transactional
    public void delete(Long id) {
        Product found = find(id);
        productRepository.delete(found);
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
}
