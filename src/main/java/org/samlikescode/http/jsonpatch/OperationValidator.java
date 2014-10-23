package org.samlikescode.http.jsonpatch;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author berrysa
 */
public final class OperationValidator {
    private OperationValidator() {}

    public static <T> T checkMemberNotNull(T member) {
        if (member == null)
            throw new OperationFormatException("Member can not be null");
        return member;
    }

    public static <T> T checkMemberNotNull(T member, RuntimeException throwableError) {
        if (member == null) {
            throw throwableError;
        }
        return member;
    }

    public static <T> T checkMemberNotNull(T member, String errorMessage) {
        if (member == null)
            throw new OperationFormatException(errorMessage);
        return member;
    }

    public static Operation validateMembersFollowRfc6902Format(Operation operation) {
        checkMemberNotNull(operation.getPath(), "path is required for all operations");
        Operation.Op op = checkMemberNotNull(operation.getOp(), "op is required for all operations");
        switch (op) {
            case ADD:
                JsonNode value = checkMemberNotNull(operation.getValue(), "value is required for add operations");
                return Operation.add(operation.getPath(), value);
            case REMOVE:
                return Operation.remove(operation.getPath());
            case COPY:
            case REPLACE:
            case TEST:
            case MOVE:
        }
        return operation;
    }
}
