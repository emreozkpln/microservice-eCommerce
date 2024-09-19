package dev.buddly.ecommerce.elasticsearch;

import co.elastic.clients.elasticsearch._types.query_dsl.*;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class EsUtil {

    public static Query createMatchAllQuery(){
        return Query.of(q->q.matchAll(new MatchAllQuery.Builder().build()));
    }

    public static Supplier<Query> buildQueryForFieldAndValue(String field, String value) {
        return () -> Query.of(q -> q.match(buildMatchQueryForFieldAndValue(field,value)));
    }

    private static MatchQuery buildMatchQueryForFieldAndValue(String field, String value) {
        return new MatchQuery.Builder()
                .field(field)
                .query(value)
                .build();
    }
}
