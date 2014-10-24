package org.samlikescode.http.jsonpatch;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import org.samlikescode.http.support.jackson.JsonPointers;

import java.util.function.Function;

import static org.samlikescode.http.jsonpatch.OperationValidator.checkMemberNotNull;

/**
 * <b>Notes:</b><ul>
 * <li> {operation}_NULLIFY_IGNORED_MEMBERS - nulls the members of the operation that are meant to be ignored. An
 * operation should not fail if ignored properties are provided. For example,
 * <pre>{"op":"remove" "path":"/a/b" "blob blob":"xyz"}</pre> is a valid {@link Operation.Op#REMOVE} operation.
 * </ul>
 */
public final class Rfc6902 {
    private Rfc6902() {}

    private static final Function<Operation.Op, Operation.Op> OP_VALIDATOR =
            op -> checkMemberNotNull(op, "op is required for all operations");

    private static final Function<JsonPointer, JsonPointer> PATH_VALIDATOR =
            p -> checkMemberNotNull(p, "path is required for all operations");

    private static final Function<JsonNode, JsonNode> VALUE_VALIDATOR =
            v -> checkMemberNotNull(v, "value is required for this operation");

    private static final Function<JsonPointer, JsonPointer> FROM_VALIDATOR =
            f -> checkMemberNotNull(f, "from is required for this operation");

    private static final Function<Operation.Builder, Operation.Builder> BASE_VALIDATOR =
            o -> {
                OP_VALIDATOR.apply(o.getOp());
                PATH_VALIDATOR.apply(o.getPath());
                return o;
            };

    private static final Function<Operation.Builder, Operation.Builder> ADD_VALIDATOR =
            o -> BASE_VALIDATOR.apply(o).value(VALUE_VALIDATOR.apply(o.getValue()));

    private static final Function<Operation.Builder, Operation.Builder> ADD_NULLIFY_IGNORED_MEMBERS =
            o -> o.from(null);

    private static final Function<Operation.Builder, Operation.Builder> REMOVE_VALIDATOR =
            BASE_VALIDATOR::apply;

    private static final Function<Operation.Builder, Operation.Builder> REMOVE_NULLIFY_IGNORED_MEMBERS =
            o -> o.from(null).value(null);

    private static final Function<Operation.Builder, Operation.Builder> REPLACE_VALIDATOR =
            o -> BASE_VALIDATOR.apply(o).value(VALUE_VALIDATOR.apply(o.getValue()));

    private static final Function<Operation.Builder, Operation.Builder> REPLACE_NULLIFY_IGNORED_MEMBERS =
            o -> o.from(null);

    private static final Function<Operation.Builder, Operation.Builder> MOVE_VALIDATOR =
            o -> {
                BASE_VALIDATOR.apply(o).from(FROM_VALIDATOR.apply(o.getFrom()));
                if (JsonPointers.IS_CHILD_POINTER.apply(o.getFrom(), o.getPath())) {
                    throw new OperationFormatException("Can't perform a move operation from a parent location to one of it's children");
                }
                return o;
            };

    private static final Function<Operation.Builder, Operation.Builder> MOVE_NULLIFY_IGNORED_MEMBERS =
            o -> o.value(null);

    private static final Function<Operation.Builder, Operation.Builder> COPY_VALIDATOR =
            o -> BASE_VALIDATOR.apply(o).from(FROM_VALIDATOR.apply(o.getFrom()));

    private static final Function<Operation.Builder, Operation.Builder> COPY_NULLIFY_IGNORED_MEMBERS =
            o -> o.value(null);

    private static final Function<Operation.Builder, Operation.Builder> TEST_VALIDATOR =
            o -> BASE_VALIDATOR.apply(o).value(VALUE_VALIDATOR.apply(o.getValue()));

    private static final Function<Operation.Builder, Operation.Builder> TEST_NULLIFY_IGNORED_MEMBERS =
            o -> o.from(null);

    public static final Function<Operation.Builder, Operation.Builder> OPERATION_VALIDATOR =
            ob -> {
                switch (ob.getOp()) {
                    case ADD:
                        return ADD_VALIDATOR.apply(ob);
                    case REMOVE:
                        return REMOVE_VALIDATOR.apply(ob);
                    case REPLACE:
                        return REPLACE_VALIDATOR.apply(ob);
                    case MOVE:
                        return MOVE_VALIDATOR.apply(ob);
                    case COPY:
                        return COPY_VALIDATOR.apply(ob);
                    case TEST:
                        return TEST_VALIDATOR.apply(ob);
                    default:
                        throw new OperationFormatException("Unsupported op of: " + ob.getOp().getValue());
                }
            };

    public static final Function<Operation.Builder, Operation.Builder> IGNORED_MEMBER_NULLIFIER =
            ob -> {
                switch (ob.getOp()) {
                    case ADD:
                        return ADD_NULLIFY_IGNORED_MEMBERS.apply(ob);
                    case REMOVE:
                        return REMOVE_NULLIFY_IGNORED_MEMBERS.apply(ob);
                    case REPLACE:
                        return REPLACE_NULLIFY_IGNORED_MEMBERS.apply(ob);
                    case MOVE:
                        return MOVE_NULLIFY_IGNORED_MEMBERS.apply(ob);
                    case COPY:
                        return COPY_NULLIFY_IGNORED_MEMBERS.apply(ob);
                    case TEST:
                        return TEST_NULLIFY_IGNORED_MEMBERS.apply(ob);
                    default:
                        throw new OperationFormatException("Unsupported op of: " + ob.getOp().getValue());
                }
            };
}
