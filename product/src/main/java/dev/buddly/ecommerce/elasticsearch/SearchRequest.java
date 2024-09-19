package dev.buddly.ecommerce.elasticsearch;

import java.util.List;

public record SearchRequest(
        List<String> fieldName,
        List<String> searchValue
) {
}
