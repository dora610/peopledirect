package link.karurisuro.peopledirect.utils;

import org.springframework.http.HttpStatus;

public class NotFoundException extends Exception {
    private final HttpStatus status;
    
    public NotFoundException(String message) {
        super(message);
        status = HttpStatus.NOT_FOUND;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
