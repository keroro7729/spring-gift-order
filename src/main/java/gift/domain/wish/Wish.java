package gift.domain.wish;

public class Wish {
    private final Long id;
    private final Long memberId;
    private final Long productId;
    private Integer quantity;

    private Wish(Long id, Long memberId, Long productId, Integer quantity) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static Wish of(Long id, Long memberId, Long productId, Integer quantity) {
        return new Wish(id, memberId, productId, quantity);
    }

    public void addQuantity(Integer addQuantity) {
        quantity += addQuantity;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
