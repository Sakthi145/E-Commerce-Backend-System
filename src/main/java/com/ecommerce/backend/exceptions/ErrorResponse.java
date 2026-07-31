package com.ecommerce.backend.exceptions;


import java.time.LocalDateTime;

public class ErrorResponse {
    private final String path;
    private final int status;
    private final LocalDateTime timestamp;
    private final String message;

    public ErrorResponse(String path, int status, LocalDateTime timestamp, String message) {
        this.path = path;
        this.status = status;
        this.timestamp = timestamp;
        this.message = message;
    }


    public String getPath() { return path; }

    public int getStatus() { return status; }

    public LocalDateTime getTimestamp() { return timestamp; }

    public String getMessage() { return message; }


}
