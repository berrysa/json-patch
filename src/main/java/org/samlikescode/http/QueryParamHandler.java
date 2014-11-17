package org.samlikescode.http;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;

import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * //todo(sb)
 */
public final class QueryParamHandler<T> {
    private final ImmutableMap<String, Predicate<T>> filters;
    private final ImmutableMap<String, BiConsumer<String, T>> consumers;

    private QueryParamHandler(ImmutableMap<String, Predicate<T>> filters,
                              ImmutableMap<String, BiConsumer<String, T>> consumers) {
        this.filters = requireNonNull(filters);
        this.consumers = requireNonNull(consumers);
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private final ImmutableMap.Builder<String, Predicate<T>> filterBuilder;
        private final ImmutableMap.Builder<String, BiConsumer<String, T>> consumerBuilder;

        private Builder() {
            this.filterBuilder = ImmutableMap.builder();
            this.consumerBuilder = ImmutableMap.builder();
        }

        public Builder<T> addFilter(String value, Predicate<T> filter) {
            filterBuilder.put(value, filter);
            return this;
        }

        public Builder<T> addConsumer(String key, BiConsumer<String, T> filter) {
            consumerBuilder.put(key, filter);
            return this;
        }

        public QueryParamHandler<T> build() {
            return new QueryParamHandler<>(filterBuilder.build(), consumerBuilder.build());
        }
    }

    public ImmutableList<T> filter(ImmutableMultimap<String, String> queryParams, ImmutableList<T> representations) {
        return applyFilters(
                filters.entrySet().stream()
                        .filter(filterEntry -> queryParams.entries().stream()
                                .filter(queryParamEntry -> QueryParam.FILTER_KEY.equals(queryParamEntry.getKey()))
                                .map(queryParamEntry -> queryParamEntry.getValue())
                                .collect(Collectors.toList())
                                .contains(filterEntry.getKey()))
                        .map(entry -> entry.getValue())
                        .iterator(),
                representations.stream()
        ).collect(GuavaCollectors.toImmutableList());
    }

    private Stream<T> applyFilters(Iterator<Predicate<T>> filterIterator, Stream<T> representationStream) {
        return !filterIterator.hasNext() ? representationStream : applyFilters(filterIterator, representationStream.filter(filterIterator.next()));
    }

    public void consume(ImmutableMultimap<String, String> queryParams, T builder) {
        consumers.entrySet().stream()
                .filter(consumerEntry -> queryParams.containsKey(consumerEntry.getKey()))
                .forEach(filteredEntry -> queryParams.get(filteredEntry.getKey()).stream()
                        .forEach(queryValue -> filteredEntry.getValue().accept(queryValue, builder)));
    }
}
