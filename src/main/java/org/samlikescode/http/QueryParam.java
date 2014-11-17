package org.samlikescode.http;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import static java.util.Objects.requireNonNull;

/**
 * //todo(sb)
 */
public class QueryParam {
    public static final String FILTER_KEY = "filter";
    public static final String POPULATE_KEY = "populate";

    private final String key;
    private final String value;

    private QueryParam(String key, String value) {
        this.key = requireNonNull(key);
        this.value = requireNonNull(value);
    }

    public static QueryParam of(String key, String value) {
        return new QueryParam(key, value);
    }

    public static QueryParam filter(String value) {
        return new QueryParam(FILTER_KEY, value);
    }

    public static QueryParam populate(String value) {
        return new QueryParam(POPULATE_KEY, value);
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("key", key)
                .add("value", value)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(key, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QueryParam that = (QueryParam) o;
        return Objects.equal(this.key, that.key)
                && Objects.equal(this.value, that.value);
    }
}
