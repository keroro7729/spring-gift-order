package gift.common.exception.code;

import gift.common.exception.ErrorCode;

public enum SecurityErrorCode implements ErrorCode {
    AUTH_MISSING_TOKEN("SEC-001"),
    AUTH_INVALID_TOKEN("SEC-002"),
    AUTH_FORBIDDEN("SEC-003"),
    LOGIN_EMAIL_NOT_FOUND("SEC-004");

    private final String code;

    SecurityErrorCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
