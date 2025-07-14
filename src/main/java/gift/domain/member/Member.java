package gift.domain.member;

public class Member {

    private final Long id;
    private final String email;
    private final String password;
    private final MemberRole role;

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
}
