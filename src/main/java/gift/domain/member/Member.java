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
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static Member of(Long id, String email, String password, MemberRole role) {
        return new Member(id, email, password, role);
    }

    public static Member createTemp(String email, String password) {
        return of(null, email, password, MemberRole.USER);
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
}
