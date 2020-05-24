package io.github.maksymsan.persistentwebapp1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "File has invalid header")
public class FileInvalidHeaderException extends RuntimeException {
    public FileInvalidHeaderException(String message) {
        super(message);
    }

    public FileInvalidHeaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileInvalidHeaderException(Throwable cause) {
        super(cause);
    }
}
