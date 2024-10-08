package dev.buddly.ecommerce.handler;

import dev.buddly.ecommerce.exception.ExceptionMessage;
import dev.buddly.ecommerce.exception.ImageNotFoundException;
import dev.buddly.ecommerce.exception.ProductNotFoundException;
import dev.buddly.ecommerce.exception.ReviewNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handle(ProductNotFoundException exp){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exp.getMsg());
    }

    @ExceptionHandler(ReviewNotFoundException.class)
    public ResponseEntity<ExceptionMessage> handle(ReviewNotFoundException exception){
        return new ResponseEntity<>(
                exception.getExceptionMessage(),
                HttpStatus.resolve(exception.getExceptionMessage().status())
        );
    }

    @ExceptionHandler(ImageNotFoundException.class)
    public ResponseEntity<ExceptionMessage> handle(ImageNotFoundException exception){
        return new ResponseEntity<>(
                exception.getExceptionMessage(),
                HttpStatus.resolve(exception.getExceptionMessage().status())
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException exp){
        var errors = new HashMap<String,String>();
        exp.getBindingResult().getAllErrors()
                .forEach(error->{
                    var fieldName = ((FieldError) error).getField();
                    var errorMessage = error.getDefaultMessage();
                    errors.put(fieldName,errorMessage);
                });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(errors));
    }
}
