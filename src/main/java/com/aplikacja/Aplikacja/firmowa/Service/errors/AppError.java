package com.aplikacja.Aplikacja.firmowa.Service.errors;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AppError {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private HttpStatus status;
    private String message;
    private String debugMessage;

    public AppError(HttpStatus httpStatus, String message, Throwable exception) {
        timestamp = LocalDateTime.now();
        this.status = httpStatus;
        this.message = message;
        this.debugMessage = exception.getLocalizedMessage();
    }
}
