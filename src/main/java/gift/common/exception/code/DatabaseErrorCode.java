package gift.common.exception.code;

import gift.common.exception.ErrorCode;

public enum DatabaseErrorCode implements ErrorCode {
    MEMBER_CREATION_FAIL("DB-001"),
    PRODUCT_CREATION_FAIL("DB-002"),
    WISH_CREATION_FAIL("DB-003");

    private final String code;

    DatabaseErrorCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
