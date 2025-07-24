package gift.common.exception;

import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String clientMessage;
    private final HttpStatus httpStatus;
    private final int logLevel;

    private BusinessException(Builder builder) {
        super(builder.logMessage);
        this.errorCode = builder.errorCode;
        this.clientMessage = builder.clientMessage;
        this.httpStatus = builder.httpStatus;
        this.logLevel = builder.logLevel;
    }

    public static BusinessException of(ErrorCode errorCode, String clientMessage, HttpStatus httpStatus) {
        return new Builder(errorCode, clientMessage)
                .httpStatus(httpStatus)
                .logLevel(1)
                .build();
    }

    public static BusinessException internal(ErrorCode errorCode, String logMessage) {
        return new Builder(errorCode, logMessage)
                .clientMessage("Sorry, internal server error.")
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .logLevel(5)
                .build();
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getClientMessage() {
        return clientMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getLogMessage() {
        return super.getMessage();
    }

    public int getLogLevel() {
        return logLevel;
    }

    public static class Builder {
        private final ErrorCode errorCode;
        private final String logMessage;

        private String clientMessage;
        private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        private int logLevel = 1;

        public Builder(@NotNull ErrorCode errorCode,
                       @NotNull String logMessage) {
            this.errorCode = errorCode;
            this.logMessage = "BusinessException(" + errorCode.getCode() + "): " + logMessage;
            this.clientMessage = logMessage;
        }

        public Builder clientMessage(String clientMessage) {
            this.clientMessage = clientMessage;
            return this;
        }

        public Builder httpStatus(HttpStatus httpStatus) {
            this.httpStatus = httpStatus;
            return this;
        }

        public Builder logLevel(int logLevel) {
            this.logLevel = logLevel;
            return this;
        }

        public BusinessException build() {
            return new BusinessException(this);
        }
    }
}
