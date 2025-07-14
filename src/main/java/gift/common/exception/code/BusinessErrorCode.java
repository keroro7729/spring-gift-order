package gift.common.exception.code;

import gift.common.exception.ErrorCode;

public enum BusinessErrorCode implements ErrorCode {
    PRODUCT_NOT_SELLING("BUS-001"),
    REGISTER_EMAIL_CONFLICT("BUS-002");

    private final String code;

    BusinessErrorCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
