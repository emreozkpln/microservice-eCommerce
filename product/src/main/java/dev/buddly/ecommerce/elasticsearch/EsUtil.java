package dev.buddly.ecommerce.elasticsearch;

import co.elastic.clients.elasticsearch._types.query_dsl.*;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class EsUtil {

    public static Query createMatchAllQuery(){
        return Query.of(q->q.matchAll(new MatchAllQuery.Builder().build()));
    }

    public static Supplier<Query> buildMultipleMatchQuery(MultipleSearchRequest request) {
        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();
        request.fieldValues().forEach((field, value) -> {
            MatchQuery matchQuery = QueryBuilders.match()
                    .field(field)
                    .query(value)
                    .build();
            boolQueryBuilder.must(Query.of(q -> q.match(matchQuery)));
        });
        return () -> Query.of(q -> q.bool(boolQueryBuilder.build()));
    }
}
