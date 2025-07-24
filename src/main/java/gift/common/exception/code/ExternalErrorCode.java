package gift.common.exception.code;

import gift.common.exception.ErrorCode;

public enum ExternalErrorCode implements ErrorCode {
    KAKAO_OAUTH_TOKEN_FAIL("EXT-001"),
    KAKAO_OAUTH_TOKEN_NETWORK_FAIL("EXT-002"),
    ;

    private final String code;

    ExternalErrorCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
