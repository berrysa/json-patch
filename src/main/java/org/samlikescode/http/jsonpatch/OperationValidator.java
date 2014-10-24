package org.samlikescode.http.jsonpatch;

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
}
