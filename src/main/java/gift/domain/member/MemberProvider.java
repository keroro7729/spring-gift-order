package gift.domain.member;

public enum MemberProvider {
    LOCAL, KAKAO;

//    private final String providerName;
//
//    MemberProvider(String providerName) {
//        this.providerName = providerName;
//    }
//
//    public static MemberProvider fromRoleName(String providerName) {
//        for (MemberProvider provider : values()) {
//            if (provider.getProviderName().equals(providerName)) {
//                return provider;
//            }
//        }
//        throw new MemberDomainRuleException("Undefined member provider name: " + providerName);
//    }
//
//    public String getProviderName() {
//        return providerName;
//    }
}
