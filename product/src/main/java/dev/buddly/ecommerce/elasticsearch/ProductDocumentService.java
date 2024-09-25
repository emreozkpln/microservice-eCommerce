package dev.buddly.ecommerce.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import dev.buddly.ecommerce.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class ProductDocumentService {

    private final ElasticsearchClient elasticsearchClient;
    private final ElasticsearchRepository elasticsearchRepository;

    public List<ProductDocument> getAllDataFromIndex(String indexName) {
        var query = EsUtil.createMatchAllQuery();
        SearchResponse<ProductDocument> response = null;
        try {
            response = elasticsearchClient.search(
                    q -> q.index(indexName).query(query), ProductDocument.class
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return extractAllResponse(response);
    }

    public List<ProductDocument> searchProductsByFieldsAndValues(MultipleSearchRequest request) {
        Supplier<Query> query = EsUtil.buildMultipleMatchQuery(request);
        SearchResponse<ProductDocument> response = null;
        try {
            response = elasticsearchClient.search(
                    q -> q.index("products_index").query(query.get()), ProductDocument.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return extractAllResponse(response);
    }

    public void deleteProductById(Integer productId){
        elasticsearchRepository.deleteById(productId);
    }

    private List<ProductDocument> extractAllResponse(SearchResponse<ProductDocument> response){
        return response
                .hits()
                .hits()
                .stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }
}
