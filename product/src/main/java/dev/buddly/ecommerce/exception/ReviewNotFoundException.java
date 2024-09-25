package dev.buddly.ecommerce.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewNotFoundException extends RuntimeException{
    private ExceptionMessage exceptionMessage;
}
