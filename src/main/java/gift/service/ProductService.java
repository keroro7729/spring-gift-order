package gift.service;

import gift.common.dto.request.ProductOptionRequestDto;
import gift.common.dto.request.ProductRequestDto;
import gift.common.dto.request.ProductUpdateRequestDto;
import gift.common.dto.response.MessageResponseDto;
import gift.common.dto.response.ProductOptionResponseDto;
import gift.common.dto.response.ProductResponseDto;
import gift.common.exception.BusinessException;
import gift.common.exception.code.BusinessErrorCode;
import gift.common.exception.code.ResourceErrorCode;
import gift.domain.product.*;
import gift.repository.ProductOptionRepository;
import gift.repository.ProductRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductOptionRepository optionRepository;

    public ProductService(ProductRepository productRepository, ProductOptionRepository optionRepository) {
        this.productRepository = productRepository;
        this.optionRepository = optionRepository;
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

    public List<ProductResponseDto> getAll(Pageable pageable) {
        return productRepository.findAll(pageable).stream()
                .map(ProductResponseDto::from)
                .toList();
    }

    public List<ProductResponseDto> getSelling(Pageable pageable) {
        return productRepository.findAllByState(pageable, ProductState.SELLING).stream()
                .map(ProductResponseDto::from)
                .toList();
    }

    @Transactional
    public MessageResponseDto<ProductResponseDto> update(Long id, ProductUpdateRequestDto body) {
        Product instance = find(id);
        instance.update(body.name(), body.price(), body.imageUrl());
        if (instance.isInvolveKakao()) {
            instance.waitApproval();
            Product updated = productRepository.save(instance);
            return new MessageResponseDto<>(false, "카카오 관련 상품 승인 대기중", 202, ProductResponseDto.from(updated));
        }
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

    @Transactional
    public ProductOption addOptionTo(Long productId, ProductOptionRequestDto request) {
        Product product = find(productId);
        ProductOption created = request.toEntity();
        product.addOption(created);
        return created;
    }

    @Transactional
    public ProductOption applyOptionSold(Long optionId, Integer soldQuantity) {
        ProductOption option = optionRepository.findById(optionId)
                .orElseThrow(() -> BusinessException.of(
                        ResourceErrorCode.PRODUCT_OPTION_NOT_FOUND,
                        "상품 옵션이 존재하지 않습니다. id = " + optionId,
                        HttpStatus.NOT_FOUND
                ));
        try {
            option.decreaseQuantity(soldQuantity);
        } catch (ProductOptionException e) {
            throw BusinessException.of(
                    BusinessErrorCode.EXCEED_PRODUCT_OPTION_QUANTITY,
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }
        return option;
    }

    public List<ProductOptionResponseDto> getOptionsOf(Long productId) {
        Product product = find(productId);
        return product.getOptions().stream()
                .map(ProductOptionResponseDto::from)
                .toList();
    }

    @Transactional
    public void deleteOptionOf(Long productId, Long optionId) {
        Product product = find(productId);
        ProductOption option = product.getOptionById(optionId);
        product.removeOption(option);
    }

    public Product findOptionsOwn(Long optionId) {
        ProductOption option = optionRepository.findById(optionId)
                .orElseThrow();
        return option.getProduct();
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
