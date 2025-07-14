package gift.repository;

import gift.domain.wish.Wish;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class WishRepository {

    private static final RowMapper<Wish> ROW_MAPPER = (rs, rowNum) -> {
        Long id = rs.getLong("id");
        Long memberId = rs.getLong("member_id");
        Long productId = rs.getLong("product_id");
        Integer quantity = rs.getInt("quantity");
        return Wish.of(id, memberId, productId, quantity);
    };
    private final JdbcClient client;

    public WishRepository(JdbcClient client) {
        this.client = client;
    }

    public Optional<Wish> save(Wish wish) {
        String sql = "insert into wish (member_id, product_id, quantity) values (:member_id, :product_id, :quantity);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            client.sql(sql)
                    .param("member_id", wish.getMemberId())
                    .param("product_id", wish.getProductId())
                    .param("quantity", wish.getQuantity())
                    .update(keyHolder, "id");
        } catch (DataAccessException e) {
            e.printStackTrace();
            return Optional.empty();
        }
        Long id = keyHolder.getKey().longValue();
        return findById(id);
    }

    public Optional<Wish> findById(Long id) {
        String sql = "select * from wish where id = :id;";
        return client.sql(sql)
                .param("id", id)
                .query(ROW_MAPPER)
                .optional();
    }

    public List<Wish> findAll() {
        String sql = "select * from wish;";
        return client.sql(sql)
                .query(ROW_MAPPER)
                .list();
    }

    public Optional<Wish> findByMemberIdProductId(Long memberId, Long productId) {
        String sql = "select * from wish where member_id = :member_id and product_id = :product_id";
        return client.sql(sql)
                .param("member_id", memberId)
                .param("product_id", productId)
                .query(ROW_MAPPER)
                .optional();
    }

    public Optional<Wish> update(Long id, Wish wish) {
        String sql = "update wish set member_id = :member_id, product_id = :product_id, quantity = :quantity where id = :id;";
        int affected = client.sql(sql)
                .param("id", id)
                .param("member_id", wish.getMemberId())
                .param("product_id", wish.getProductId())
                .param("quantity", wish.getQuantity())
                .update();

        if (affected == 0) {
            return Optional.empty();
        }
        return findById(id);
    }

    public void delete(Long id) {
        String sql = "delete from wish where id = :id;";
        client.sql(sql)
                .param("id", id)
                .update();
    }
}
