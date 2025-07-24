package gift.common.exception.code;

import gift.common.exception.ErrorCode;

public enum ValidationErrorCode implements ErrorCode {
    MEMBER_EMAIL_EMPTY("VAL-001"),
    MEMBER_EMAIL_WRONG_FORMAT("VAL-002"),
    MEMBER_PASSWORD_EMPTY("VAL-003"),
    MEMBER_PASSWORD_INVALID("VAL-004")
    ;

    private final String code;

    ValidationErrorCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
