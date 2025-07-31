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

    @Column(name = "product_option_id", nullable = false)
    private Long productOptionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_id", insertable = false, updatable = false)
    private ProductOption productOption;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private LocalDateTime orderDateTime;

    @Column
    private String message;

    protected Order(){
    }

    private Order(Long id, Long productOptionId, Integer quantity, LocalDateTime orderDateTime, String message) {
        this.id = id;
        this.productOptionId = productOptionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
    }

    public static Order of(Long productOptionId, Integer quantity, String message) {
        return new Order(null, productOptionId, quantity, LocalDateTime.now(), message);
    }

    public String getPurchaseMessage() {
        return String.format("고객님께서 구매하신 상품 %s이 주문 완료되었습니다.", productOption.getName());
    }

    public Long getId() {
        return id;
    }

    public Long getProductOptionId() {
        return productOptionId;
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
