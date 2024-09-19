package dev.buddly.ecommerce.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ElasticSearchRepository extends ElasticsearchRepository<ProductDocument,String> {
}
