package gift.domain.wish;

import gift.domain.member.Member;
import gift.domain.product.Product;
import jakarta.persistence.*;

@Entity
@Table(
        name = "wish",
        uniqueConstraints = @UniqueConstraint(columnNames = {"member_id", "product_id"})
)
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    public Wish() {

    }

    private Wish(Long id, Member member, Product product, Integer quantity) {
        this.id = id;
        this.member = member;
        this.product = product;
        this.quantity = quantity;
    }

    public static Wish of(Long id, Member member, Product product, Integer quantity) {
        return new Wish(id, member, product, quantity);
    }

    public void addQuantity(Integer addQuantity) {
        quantity += addQuantity;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Long getMemberId() {
        return member.getId();
    }

    public Product getProduct() {
        return product;
    }

    public Long getProductId() {
        return product.getId();
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isOwner(Long id) {
        return getMemberId().equals(id);
    }
}
