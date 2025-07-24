package gift.domain.product;

public enum ProductState {
    SELLING("판매중"),
    WAITING("승인대기중"),
    TEMP("임시생성"),
    REJECTED("승인거절됨");

    private final String stateName;

    ProductState(String stateName) {
        this.stateName = stateName;
    }

    public static ProductState fromStateName(String stateName) {
        for (ProductState state : values()) {
            if (state.stateName.equals(stateName)) {
                return state;
            }
        }
        throw new ProductStateException("존재하지 않는 stateName: " + stateName);
    }

    public String getStateName() {
        return stateName;
    }
}
