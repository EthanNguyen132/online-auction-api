package edu.miu.waa.onlineauctionapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = {RecordNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorResponse resourceNotFoundException(RecordNotFoundException ex, WebRequest request) {
        ErrorResponse message = ErrorResponse.create(
                ex, HttpStatus.NOT_FOUND, "Record Not Found!"
        );
        return message;
    }
}
