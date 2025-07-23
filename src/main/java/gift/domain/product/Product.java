package gift.domain.product;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "product")
public class Product {

    public static final Long MAX_PRICE = 9999999999L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long price;

    @Column(name = "image_url")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductState state;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductOption> options = new ArrayList<>();

    protected Product() {
    }

    private Product(Long id, String name, Long price, String imageUrl, ProductState state) {
        this.id = id;
        validateName(name);
        this.name = name;
        validatePrice(price);
        this.price = price;
        this.imageUrl = imageUrl;
        validateState(state);
        this.state = state;
    }

    public static Product of(Long id, String name, Long price, String imageUrl, ProductState state) {
        return new Product(id, name, price, imageUrl, state);
    }

    public static Product tempInstance(String name, Long price, String imageUrl) {
        return of(null, name, price, imageUrl, ProductState.TEMP);
    }

    public static Product create(String name, Long price, String imageUrl, ProductOption option) {
        Product created = new Product(null, name, price, imageUrl, ProductState.TEMP);
        created.addOption(option);
        return created;
    }

    public boolean isInvolveKakao()  {
        return name.matches(".*카카오.*");
    }

    public void addOption(ProductOption option) {
        if (option == null) {
            throw new ProductDomainRuleException("옵션은 null 일 수 없습니다!");
        }
        options.add(option);
        option.setProduct(this);
    }

    public void removeOption(ProductOption option) {
        options.remove(option);
        option.setProduct(null);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ProductState getState() {
        return state;
    }

    public String getStateName() {
        return state.getStateName();
    }

    public List<ProductOption> getOptions() {
        return List.copyOf(options);
    }

    public Optional<ProductOption> getOptionById(Long id) {
        for (ProductOption o : options) {
            if (id.equals(o.getId())) {
                return Optional.of(o);
            }
        }
        return Optional.empty();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void update(String name, Long price, String imageUrl) {
        validateName(name);
        validatePrice(price);
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public void onBoard() {
        if (state != ProductState.TEMP) {
            throw new ProductStateException("cannot change Product state onBoard: " + this);
        }
        state = ProductState.SELLING;
    }

    public void waitApproval() {
        state = ProductState.WAITING;
    }

    public boolean isShowable(ProductQueryOption option) {
        return switch (option) {
            case ALL -> true;
            case SELLING -> state == ProductState.SELLING;
        };
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new ProductDomainRuleException("상품명 필수!");
        }
        if (!name.matches("^[A-Za-z가-힣0-9()\\[\\]+\\-&/_ ]{1,15}$")) {
            throw new ProductDomainRuleException("상품명은 15자 이하의 영문, 한글, 숫자 및 특수기호 ()[]+-&/_만 허용됨: " + name);
        }
    }

    private void validatePrice(Long price) {
        if (price == null) {
            throw new ProductDomainRuleException("상품 가격 필수!");
        }
        if (price < 0 || MAX_PRICE < price) {
            throw new ProductDomainRuleException("상품 가격은 10자리 이하의 양수여야함: " + price);
        }
    }

    private void validateState(ProductState state) {
        if (state == null) {
            throw new ProductStateException("상품 상태 필수!");
        }
    }

    @Override
    public String toString() {
        return "{" + id + ", " + name + ", " + price + ", " + state + "}";
    }
}
