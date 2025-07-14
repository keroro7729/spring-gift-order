package gift.domain.product;

public class Product {

    public static final Long MAX_PRICE = 9999999999L;

    private final Long id;
    private final String name;
    private final Long price;
    private final String imageUrl;
    private ProductState state;

    private Product(Long id, String name, Long price, String imageUrl, ProductState state) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.state = state;
    }

    public static Product of(Long id, String name, Long price, String imageUrl, ProductState state) {
        if (name == null || name.isBlank()) {
            throw new ProductDomainRuleException("상품명 필수!");
        }
        if (!name.matches("^[A-Za-z가-힣0-9()\\[\\]+\\-&/_ ]{1,15}$")) {
            throw new ProductDomainRuleException("상품명은 15자 이하의 영문, 한글, 숫자 및 특수기호 ()[]+-&/_만 허용됨: " + name);
        }
        if (price == null) {
            throw new ProductDomainRuleException("상품 가격 필수!");
        }
        if (price < 0 || MAX_PRICE < price) {
            throw new ProductDomainRuleException("상품 가격은 10자리 이하의 양수여야함: " + price);
        }
        if (state == null) {
            throw new ProductStateException("상품 상태 필수!");
        }
        return new Product(id, name, price, imageUrl, state);
    }

    public static Product tempInstance(String name, Long price, String imageUrl) {
        return of(null, name, price, imageUrl, ProductState.TEMP);
    }

    public boolean involveKakao() {
        return name.matches(".*카카오.*");
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

    public String getStateName() {
        return state.getStateName();
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

    public void approve() {
        if (state != ProductState.WAITING) {
            throw new ProductStateException("Product is not waiting for approve: " + this);
        }
        state = ProductState.SELLING;
    }

    public void reject() {
        if (state != ProductState.WAITING) {
            throw new ProductStateException("Product is not waiting for approve: " + this);
        }
        state = ProductState.REJECTED;
    }

    public boolean isShowable(ProductQueryOption option) {
        return switch (option) {
            case ALL -> true;
            case SELLING -> state == ProductState.SELLING;
        };
    }

    @Override
    public String toString() {
        return "{" + id + ", " + name + ", " + price + ", " + state + "}";
    }
}
