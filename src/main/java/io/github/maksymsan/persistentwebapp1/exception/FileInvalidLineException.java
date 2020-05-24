package io.github.maksymsan.persistentwebapp1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "A line in the file has invalid format")
public class FileInvalidLineException extends RuntimeException {
    public FileInvalidLineException(String message) {
        super(message);
    }

    public FileInvalidLineException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileInvalidLineException(Throwable cause) {
        super(cause);
    }
}
