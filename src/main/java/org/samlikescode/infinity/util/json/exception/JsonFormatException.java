package org.samlikescode.infinity.util.json.exception;

import org.samlikescode.infinity.response.Message;

/**
 * //todo(sb)
 */
public class JsonFormatException extends RuntimeException {
    private final Message error;

    public JsonFormatException(Message error) {
        super("Invalid JSON failed with errors: " + error);
        this.error = error;
    }

    public static JsonFormatException missingField(String field, String message) {
        throw new JsonFormatException(Message.missingField(field, message));
    }

    public Message getError() {
        return error;
    }
}
