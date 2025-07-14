package gift.repository;

import gift.domain.member.Member;
import gift.domain.member.MemberRole;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepository {

    private final JdbcClient client;
    private static final RowMapper<Member> ROW_MAPPER = (rs, rowNum) -> {
        Long id = rs.getLong("id");
        String email = rs.getString("email");
        String password = rs.getString("password");
        String roleName = rs.getString("role");
        return Member.of(id, email, password, MemberRole.fromRoleName(roleName));
    };

    public MemberRepository(JdbcClient client) {
        this.client = client;
    }

    public Optional<Member> save(Member member) {
        String sql = "insert into member (email, password, role) values (:email, :password, :role);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            client.sql(sql)
                    .param("email", member.getEmail())
                    .param("password", member.getPassword())
                    .param("role", member.getRoleName())
                    .update(keyHolder, "id");
        } catch (DataAccessException e) {
            return Optional.empty();
        }
        Long id = keyHolder.getKey().longValue();
        return findById(id);
    }

    public Optional<Member> findById(Long id) {
        String sql = "select * from member where id = :id;";
        return client.sql(sql)
                .param("id", id)
                .query(ROW_MAPPER)
                .optional();
    }

    public Optional<Member> findByEmail(String email) {
        String sql = "select * from member where email = :email;";
        return client.sql(sql)
                .param("email", email)
                .query(ROW_MAPPER)
                .optional();
    }

    public List<Member> findAll() {
        String sql = "select * from member;";
        return client.sql(sql)
                .query(ROW_MAPPER)
                .list();
    }

    public Optional<Member> update(Long id, Member member) {
        String sql = "update member set email = :email, password = :password, role = :role where id = :id;";
        int affected = client.sql(sql)
                .param("id", id)
                .param("email", member.getEmail())
                .param("password", member.getPassword())
                .param("role", member.getRoleName())
                .update();

        if (affected == 0) {
            return Optional.empty();
        }
        return findById(id);
    }

    public void delete(Long id) {
        String sql = "delete from member where id = :id;";
        client.sql(sql)
                .param("id", id)
                .update();
    }
}
