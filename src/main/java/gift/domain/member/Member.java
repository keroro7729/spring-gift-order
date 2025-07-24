package gift.domain.member;

import jakarta.persistence.*;

@Entity
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role;

    protected Member() {
    }

    private Member(Long id, String email, String password, MemberRole role) {
        this.id = id;
        validateEmail(email);
        this.email = email;
        validatePassword(password);
        this.password = password;
        validateRole(role);
        this.role = role;
    }

    public static Member of(Long id, String email, String password, MemberRole role) {
        return new Member(id, email, password, role);
    }

    public static Member createTemp(String email, String password) {
        return new Member(null, email, password, MemberRole.USER);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public MemberRole getRole() {
        return role;
    }

    public String getRoleName() {
        return role.getRoleName();
    }

    public void setId(Long id) {
        this.id = id;
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new MemberDomainRuleException("이메일은 필수입니다.");
        }
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new MemberDomainRuleException("이메일 형식이 아닙니다: "+email);
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new MemberDomainRuleException("비밀번호에 null 또는 빈값이 할당됨!!");
        }
    }

    private void validateRole(MemberRole role) {
        if (role == null) {
            throw new MemberDomainRuleException("Member Role이 설정되지 않았습니다. null");
        }
    }
}
