package dev.buddly.imageservice.handler;

import java.util.Map;

public record ErrorResponse(
        Map<String,String> errors
) {
}
