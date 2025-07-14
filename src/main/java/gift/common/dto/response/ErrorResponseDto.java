package gift.common.dto.response;

import gift.common.exception.BusinessException;

public record ErrorResponseDto(String message, int code) {
    public static ErrorResponseDto from(BusinessException e) {
        return new ErrorResponseDto(e.getClientMessage(), e.getHttpStatus().value());
    }
}
