package gift.domain.product;

import jakarta.persistence.*;

@Entity
@Table(name = "product_option")
public class ProductOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    protected ProductOption() {

    }

    private ProductOption(Long id, String name, Integer quantity, Product product) {
        validateName(name);
        validateQuantity(quantity);
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    public static ProductOption of(String name, Integer quantity) {
        return new ProductOption(null, name, quantity, null);
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new ProductOptionException("상품 옵션 이름은 필수값입니다.");
        }
        if (!name.matches("^[A-Za-z가-힣0-9()\\[\\]+\\-&/_ ]{1,50}$")) {
            throw new ProductOptionException("상품 옵션 이름은 50자 이하 한글, 영문, 숫자, 특수문자로 이루어져있어야 합니다.  ( ), [ ], +, -, &, /, _");
        }
    }

    private void validateQuantity(Integer quantity) {
        if (quantity == null) {
            throw new ProductOptionException("상품 옵션 수량은 필수값입니다.");
        }
        if (quantity <= 0 || 100000000 <= quantity) {
            throw new ProductOptionException("상품 옵션 수량은 최소 1개 이상 1억 개 미만");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void decreaseQuantity(Integer soldQuantity) {
        if (this.quantity < soldQuantity) {
            throw new ProductOptionException("상품 옵션 수량을 초과한 주문이 거절되었습니다. 남은수량: " + this.quantity);
        }
        this.quantity -= soldQuantity;
    }
}
