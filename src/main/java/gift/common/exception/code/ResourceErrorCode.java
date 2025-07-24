package gift.common.exception.code;

import gift.common.exception.ErrorCode;

public enum ResourceErrorCode implements ErrorCode {
    PRODUCT_NOT_FOUND("RES-001"),
    MEMBER_NOT_FOUND("RES-002"),
    WISH_NOT_FOUND("RES-003"),
    PRODUCT_OPTION_NOT_FOUND("RES-004"),
    ;

    private final String code;

    ResourceErrorCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
