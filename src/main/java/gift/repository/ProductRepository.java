package gift.repository;

import gift.domain.product.Product;
import gift.domain.product.ProductState;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {

    private final JdbcClient client;
    private static final RowMapper<Product> ROW_MAPPER = (rs, rowNum) -> {
        Long id = rs.getLong("id");
        String name = rs.getString("name");
        Long price = (long) rs.getInt("price");
        String imageUrl = rs.getString("image_url");
        String stateName = rs.getString("state");
        return Product.of(id, name, price, imageUrl, ProductState.fromStateName(stateName));
    };

    public ProductRepository(JdbcClient client) {
        this.client = client;
    }

    public Optional<Product> findById(Long id) {
        String sql = "select * from product where id=:id;";
        return client.sql(sql)
                .param("id", id)
                .query(ROW_MAPPER)
                .optional();
    }

    public List<Product> findAll() {
        String sql = "select * from product;";
        return client.sql(sql)
                .query(ROW_MAPPER)
                .list();
    }

    public Optional<Product> save(Product product) {
        String sql = "insert into product (name, price, image_url, state) values (:name, :price, :image_url, :state);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            client.sql(sql)
                    .param("name", product.getName())
                    .param("price", product.getPrice())
                    .param("image_url", product.getImageUrl())
                    .param("state", product.getStateName())
                    .update(keyHolder, "id");
        } catch (DataAccessException e) {
            return Optional.empty();
        }
        Long id = keyHolder.getKey().longValue();
        return findById(id);
    }

    public void delete(Long id) {
        String sql = "delete from product where id = :id;";
        client.sql(sql)
                .param("id", id)
                .update();
    }

    public Optional<Product> update(Long id, Product product) {
        String sql = "update product set name = :name, price = :price, image_url = :image_url, state = :state where id = :id;";
        int affected = client.sql(sql)
                .param("id", id)
                .param("name", product.getName())
                .param("price", product.getPrice())
                .param("image_url", product.getImageUrl())
                .param("state", product.getStateName())
                .update();

        if (affected == 0) {
            return Optional.empty();
        }
        return findById(id);
    }
}
