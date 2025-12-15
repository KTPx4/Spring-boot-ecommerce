package ecommerce.api.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseBuilder {

    public static <T> ResponseEntity<ResponeCustom<T>> success(String message, T data) {
        return new ResponseEntity<>(
                new ResponeCustom<>(message, data, HttpStatus.OK.value()),
                HttpStatus.OK
        );
    }

    public static <T> ResponseEntity<ResponeCustom<T>> success(String message) {
        return new ResponseEntity<>(
                new ResponeCustom<>(message, null, HttpStatus.OK.value()),
                HttpStatus.OK
        );
    }

    public static <T> ResponseEntity<ResponeCustom<T>> error(String message, HttpStatus status) {
        return new ResponseEntity<>(
                new ResponeCustom<>(message, null, status.value()),
                status
        );
    }

    public static <T> ResponseEntity<ResponeCustom<T>> error(String message, int code) {
        return new ResponseEntity<>(
                new ResponeCustom<>(message, null, code),
                HttpStatus.valueOf(code)
        );
    }

    public static <T> ResponseEntity<ResponeCustom<T>> unAuthorized() {
        return new ResponseEntity<>(
                new ResponeCustom<>("Please login", null, HttpStatus.UNAUTHORIZED.value()),
                HttpStatus.UNAUTHORIZED
        );
    }
    public static <T> ResponseEntity<ResponeCustom<T>> badRequest(String message) {
        return new ResponseEntity<>(
                new ResponeCustom<>(message, null, HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST
        );
    }
    public static <T> ResponseEntity<ResponeCustom<T>> serverError(String message) {
        return new ResponseEntity<>(
                new ResponeCustom<>(message, null, HttpStatus.INTERNAL_SERVER_ERROR.value()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @Data
    @AllArgsConstructor
    public static class ResponeCustom<T> {
        private String message;
        private T data;
        private int code;
    }
}
