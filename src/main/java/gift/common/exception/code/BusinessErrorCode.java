package gift.common.exception.code;

import gift.common.exception.ErrorCode;

public enum BusinessErrorCode implements ErrorCode {
    PRODUCT_NOT_SELLING("BUS-001"),
    REGISTER_EMAIL_CONFLICT("BUS-002"),
    UNKNOWN_PRODUCT_QUERY_OPTION("BUS-003"),
    EXCEED_PRODUCT_OPTION_QUANTITY("BUS-004"),
    ;

    private final String code;

    BusinessErrorCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
