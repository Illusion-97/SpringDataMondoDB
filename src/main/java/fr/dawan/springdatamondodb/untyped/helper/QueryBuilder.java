package fr.dawan.springdatamondodb.untyped.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.MongoRegexCreator;
import org.springframework.data.mongodb.core.query.Query;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class QueryBuilder {

    private static <T> T parse(String input,@NonNull T defaultValue) {
        try {
            return new ObjectMapper().readValue(Objects.toString(input, ""), new TypeReference<>() {});
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private static List<String> getFilteredKeys(Map<String, String> params) {
        return params.keySet().stream()
                .filter(key -> Stream.of("page", "size", "sort", "include", "exclude").noneMatch(key::equals)).toList();
    }

    private static List<Criteria> getCriteriaList(Map<String, String> params, List<String> stringList) {
        return stringList.stream()
                .map(key -> {
                    String[] map = key.split("\\*");
                    return getCriteria(map[0], map.length > 1 ? map[1] : "is", params.get(key));
                }).toList();
    }

    private static Criteria getCriteria(String field, String methodName, String stringValue) {
        Criteria criteria = Criteria.where(field);
        return methodName.equals("regex") ? criteria.regex(getRegex(stringValue))
                : invokeCriteriaMethod(methodName, Object.class, criteria, parse(stringValue, new Object()),
                () -> invokeCriteriaMethod(methodName, Collection.class, criteria, parse(stringValue, new ArrayList<>()),
                        () -> criteria.is(stringValue)));
    }

    private static <T> Criteria invokeCriteriaMethod(String methodName, Class<T> argumentClass, Criteria criteria, T argument, Supplier<Criteria> orElse) {
        try {
            return (Criteria) Criteria.class.getDeclaredMethod(methodName, argumentClass).invoke(criteria, argument);
        } catch (Exception e) {
            return orElse.get();
        }
    }

    private static String getRegex(String value) {
        return Objects.requireNonNull(
                MongoRegexCreator.INSTANCE.toRegularExpression(value, MongoRegexCreator.MatchMode.REGEX));
    }

    public static Query build(Map<String, String> params, Pageable pageable) {
        List<String> keys = getFilteredKeys(params);
        Criteria criteria = new Criteria();
        if (!keys.isEmpty()) criteria.andOperator(getCriteriaList(params, keys));
        return applyFilters(new Query(criteria), params).with(pageable);
    }

    private static Query applyFilters(Query query, Map<String, String> params) {
        ArrayList<String> include = parse(params.get("include"), new ArrayList<>());
        if (!include.isEmpty())query.fields().include(include.toArray(new String[]{}));
        ArrayList<String> exclude = parse(params.get("exclude"), new ArrayList<>());
        if (!include.isEmpty())query.fields().exclude(exclude.toArray(new String[]{}));
        return query;
    }
}
