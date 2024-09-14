package dev.buddly.imageservice.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ImageNotFoundException extends RuntimeException {
    private final String msg;
}
