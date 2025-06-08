package com.tekerasoft.tekeramarketplace.utils;


import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.util.ObjectBuilder;
import com.tekerasoft.tekeramarketplace.dto.request.EsSearchRequest;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class EsUtil {

    public static Query createMatchAllQuery(){
        return Query.of(q -> q.matchAll(new MatchAllQuery.Builder().build()));
    }

    public static Supplier<Query> buildQueryForFieldAndValue(@NotNull String fieldName, @NotNull String searchValue) {
        return () -> Query.of(q -> q.match(buildMatchQueryForFieldAndValue(fieldName,searchValue)));
    }

    private static MatchQuery buildMatchQueryForFieldAndValue(String fieldName, String searchValue) {
        return new MatchQuery.Builder()
                .field(fieldName)
                .query(searchValue)
                .build();
    }

    public static Supplier<Query> createBoolQuery(EsSearchRequest req) {
        return () -> Query.of(q -> q.bool(boolQuery(
                req.getFieldName().get(0),req.getSearchValue().get(0),
                req.getFieldName().get(1), req.getSearchValue().get(1))
        ));
    }

    private static BoolQuery boolQuery(String key1, String value1, String key2, String value2) {
        return new BoolQuery.Builder()
                .filter(termQuery(key1,value1))
                .must(matchQuery(key2,value2))
                .build();
    }

    private static Query termQuery(String field, String value) {
        return Query.of(q -> q.term(
                new TermQuery.Builder()
                        .field(field)
                        .value(value)
                        .build()
        ));
    }

    private static Query matchQuery(String field, String value) {
        return Query.of(q -> q.match(new MatchQuery.Builder()
                .field(field)
                .query(value)
                .build()
        ));
    }

    public static Query buildAutoSuggestQuery(String name) {
        return Query.of(q -> q.match(new MatchQuery.Builder()
                .field("name")
                .query(name)
                        .analyzer("custom_index")
                .build()
        ));
    }
}
