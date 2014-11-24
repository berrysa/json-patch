package org.samlikescode.infinity.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import java.util.Optional;

/**
 * Null field implies global message
 * //todo(sb)
 */
public class Message {
    private final int code;
    private final Optional<String> field;
    private final String message;

    @JsonCreator
    public Message(@JsonProperty("code") int code,
                   @JsonProperty("field") String field,
                   @JsonProperty("message") String message) {
        this.code = code;
        this.field = Optional.ofNullable(field);
        this.message = message;
    }

    public static Message forField(int code, String field, String message) {
        return new Message(code, field, message);
    }

    public static Message missingField(String field, String message) {
        return forField(MessageCode.MISSING, field, message);
    }

    public static Message of(int code, String message) {
        return new Message(code, null, message);
    }

    public int getCode() {
        return code;
    }

    public Optional<String> getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("code", code)
                .add("field", field)
                .add("message", message)
                .toString();
    }
}
