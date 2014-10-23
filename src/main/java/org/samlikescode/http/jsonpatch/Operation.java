package org.samlikescode.http.jsonpatch;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Map;

import static org.samlikescode.http.jsonpatch.Operation.Op.ADD;
import static org.samlikescode.http.jsonpatch.Operation.Op.REMOVE;

/**
 * @author berrysa
 */
public class Operation {
    private final Op op;
    private final JsonPointer path;
    private final JsonNode value;
    private final JsonPointer from;

    private Operation(Builder builder) {
        this.op = builder.op;
        this.path = builder.path;
        this.value = builder.value;
        this.from = builder.from;
    }

    public static Operation add(JsonPointer path, JsonNode value) {
        return Operation.builder(ADD, path).value(value).build();
    }

    public static Operation remove(JsonPointer path) {
        return Operation.builder(REMOVE, path).build();
    }

    @JsonCreator
    public static Operation deserializationFactory(@JsonProperty("op") Op op,
                                                   @JsonProperty("path") JsonPointer path,
                                                   @JsonProperty("value") JsonNode value,
                                                   @JsonProperty("from") JsonPointer from) {
        return Operation.builder(op, path).value(value).from(from).build();
    }

    public Op getOp() {
        return op;
    }

    public JsonPointer getPath() {
        return path;
    }

    public JsonNode getValue() {
        return value;
    }

    public JsonPointer getFrom() {
        return from;
    }

    public static Builder builder(Op op, JsonPointer path) {
        return new Builder(op, path);
    }

    public static class Builder {
        private final Op op;
        private final JsonPointer path;
        private JsonNode value;
        private JsonPointer from;

        public Builder(Op op, JsonPointer path) {
            this.op = op;
            this.path = path;
        }

        public Builder value(JsonNode value) {
            this.value = value;
            return this;
        }

        public Builder from(JsonPointer from) {
            this.from = from;
            return this;
        }

        public Op getOp() {
            return op;
        }

        public JsonPointer getPath() {
            return path;
        }

        public JsonNode getValue() {
            return value;
        }

        public JsonPointer getFrom() {
            return from;
        }

        public Operation build() {
            return new Operation(Rfc6902.IGNORED_MEMBER_NULLIFIER
                    .andThen(Rfc6902.OPERATION_VALIDATOR).apply(this));
        }
    }

    public enum Op {
        ADD("add"),
        REMOVE("remove"),
        REPLACE("replace"),
        MOVE("move"),
        COPY("copy"),
        TEST("test");

        private final String value;

        private Op(String value) {
            this.value = value;
        }

        private static Map<String, Op> stringMap = Maps.newHashMap();
        static {
            Lists.newArrayList(Op.values()).forEach(o -> stringMap.put(o.getValue(), o));
        }

        public String getValue() {
            return value;
        }

        public static Op fromString(String value) throws Throwable {
            Op result = stringMap.get(value);
            if (result == null)
                throw new OperationFormatException("Unsupported op value: " + value);
            return result;
        }
    }
}
