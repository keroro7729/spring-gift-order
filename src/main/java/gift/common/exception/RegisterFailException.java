package gift.common.exception;

import org.springframework.http.HttpStatus;

public class RegisterFailException extends RuntimeException {
    private final HttpStatus status;

    public RegisterFailException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
