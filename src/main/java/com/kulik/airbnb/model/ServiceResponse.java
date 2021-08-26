package com.kulik.airbnb.model;

import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ServiceResponse<T> {
    private String message;
    private T body;

    public ServiceResponse(String message, T body) {
        this.message = message;
        this.body = body;
    }

    public static ResponseEntity returnResponseEntity(ServiceResponse response) {
        if (response.getMessage().equals("ok")) {
            return ResponseEntity.ok(response.getBody());
        } else {
            return new ResponseEntity<>(response.getMessage(), HttpStatus.CONFLICT);
        }
    }

    public String getMessage() {
        return message;
    }

    public T getBody() {
        return body;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
