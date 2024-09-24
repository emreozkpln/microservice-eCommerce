package dev.buddly.ecommerce.elasticsearch;

import java.util.Map;

public record MultipleSearchRequest(
        Map<String,String> fieldValues
) {
}
