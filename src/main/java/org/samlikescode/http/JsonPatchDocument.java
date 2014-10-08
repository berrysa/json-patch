package org.samlikescode.http;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonPointer;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.samlikescode.http.JsonPatchDocument.Operation.ADD;
import static org.samlikescode.http.JsonPatchDocument.Operation.COPY;
import static org.samlikescode.http.JsonPatchDocument.Operation.MOVE;
import static org.samlikescode.http.JsonPatchDocument.Operation.REMOVE;
import static org.samlikescode.http.JsonPatchDocument.Operation.REPLACE;
import static org.samlikescode.http.JsonPatchDocument.Operation.TEST;

/**
 * @see <a href="https://tools.ietf.org/html/rfc6902">RFC 6902</a> for JSON PATCH spec
 * @see <a href="https://tools.ietf.org/html/rfc6901">RFC 6901</a> for JSON Pointer spec
 */
public class JsonPatchDocument<T> {
    private final Operation op;
    private final JsonPointer path;
    private final Optional<T> value;
    private final Optional<JsonPointer> from;

    @JsonCreator
    public JsonPatchDocument(@JsonProperty("op") Operation op,
                             @JsonProperty("path") JsonPointer path,
                             @JsonProperty("value") T value,
                             @JsonProperty("from") JsonPointer from) {
        this.op = checkNotNull(op);
        this.path = checkNotNull(path);
        this.value = Optional.fromNullable(value);
        this.from = Optional.fromNullable(from);
    }

    public static <T> JsonPatchDocument<T> add(T value, JsonPointer path) {
        return new JsonPatchDocument<T>(ADD, path, value, null);
    }

    public static <T> JsonPatchDocument<T> remove(JsonPointer path) {
        return new JsonPatchDocument<T>(REMOVE, path, null, null);
    }

    public static <T> JsonPatchDocument<T> replace(T value, JsonPointer path) {
        return new JsonPatchDocument<T>(REPLACE, path, value, null);
    }

    public static <T> JsonPatchDocument<T> move(T value, JsonPointer to, JsonPointer from) {
        return new JsonPatchDocument<T>(MOVE, to, value, from);
    }

    public static <T> JsonPatchDocument<T> copy(T value, JsonPointer to, JsonPointer from) {
        return new JsonPatchDocument<T>(COPY, to, value, from);
    }

    public static <T> JsonPatchDocument<T> test(T expected, JsonPointer path) {
        return new JsonPatchDocument<T>(TEST, path, expected, null);
    }

    public Operation getOp() {
        return op;
    }

    public JsonPointer getPath() {
        return path;
    }

    public Optional<T> getValue() {
        return value;
    }

    public Optional<JsonPointer> getFrom() {
        return from;
    }

    public enum Operation {
        ADD("add"),
        REMOVE("remove"),
        REPLACE("replace"),
        MOVE("move"),
        COPY("copy"),
        TEST("test");

        private final String value;

        private Operation(String value) {
            this.value = value;
        }

        private static final ImmutableMap<String, Operation> OPERATION_VALUE_MAP;

        static {
            ImmutableMap.Builder<String, Operation> operationMap = ImmutableMap.builder();
            for (Operation op : values()) {
                operationMap.put(op.getValue(), op);
            }
            OPERATION_VALUE_MAP = operationMap.build();
        }

        @JsonCreator
        public static Optional<Operation> fromString(String code) {
            return Optional.fromNullable(OPERATION_VALUE_MAP.get(code));
        }

        @JsonValue
        public String getValue() {
            return value;
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("op", op)
                .add("path", path)
                .add("value", value)
                .add("from", from)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JsonPatchDocument that = (JsonPatchDocument) o;
        return Objects.equal(this.op, that.op)
                && Objects.equal(this.path, that.path)
                && Objects.equal(this.value, that.value)
                && Objects.equal(this.from, that.from);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(
                op,
                path,
                value,
                from);
    }
}
