package com.example.newsproject.exception;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс для представления ошибки.
 * Содержит информацию о времени ошибки, сообщении об ошибке и подробностях.
 */
@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {

    private String timestamp;
    private int status;
    private String error;
    private String message;

    public ErrorResponse(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = LocalDateTime.now().format(ISO_LOCAL_DATE_TIME);
    }

}
