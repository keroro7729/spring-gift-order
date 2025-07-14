package gift.domain.member;

public enum MemberRole {
    USER("사용자"), ADMIN("관리자"), MD("상품기획자");

    private final String roleName;

    MemberRole(String roleName) {
        this.roleName = roleName;
    }

    public static MemberRole fromRoleName(String roleName) {
        for (MemberRole role : values()) {
            if (role.roleName.equals(roleName)) {
                return role;
            }
        }
        throw new MemberRoleException("Undefined member role name: " + roleName);
    }

    public String getRoleName() {
        return roleName;
    }
}
