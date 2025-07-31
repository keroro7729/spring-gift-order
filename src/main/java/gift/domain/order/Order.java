package gift.domain.order;

import gift.domain.product.ProductOption;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_id", nullable = false)
    private ProductOption productOption;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private LocalDateTime orderDateTime;

    @Column
    private String message;

    protected Order(){
    }

    private Order(Long id, ProductOption productOption, Integer quantity, LocalDateTime orderDateTime, String message) {
        this.id = id;
        this.productOption = productOption;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
    }

    public static Order of(ProductOption productOption, Integer quantity, String message) {
        return new Order(null, productOption, quantity, LocalDateTime.now(), message);
    }

    public String getPurchaseMessage() {
        return String.format("고객님께서 구매하신 상품 %s이 주문 완료되었습니다.", getProductOption().getName());
    }

    public Long getId() {
        return id;
    }

    public ProductOption getProductOption() {
        return productOption;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }
}
