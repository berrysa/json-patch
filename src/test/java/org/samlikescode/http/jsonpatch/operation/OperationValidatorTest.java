package org.samlikescode.http.jsonpatch.operation;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.node.BooleanNode;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.samlikescode.http.jsonpatch.Operation;
import org.samlikescode.http.jsonpatch.OperationFormatException;
import org.samlikescode.http.jsonpatch.OperationValidator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
/*
Members that are not explicitly defined for the operation in question MUST be ignored
 */
@Ignore
public class OperationValidatorTest {
    private Operation.Builder addBuilder;

    @Before
    public void setUp() throws Exception {
        addBuilder = Operation.builder(Operation.Op.ADD, JsonPointer.compile("/some/property/path"));
    }

    @Test(expected = OperationFormatException.class)
    public void testCheckMemberNotNull_nullMember() throws Exception {
        OperationValidator.checkMemberNotNull(null);
        fail("null member should throw exception");
    }

    @Test
    public void testCheckMemberNotNull_notNullMember() throws Exception {
        assertNotNull(OperationValidator.checkMemberNotNull(4));
    }

    @Test(expected = FakeException.class)
    public void testCheckMemberNotNull_provideThrowable() throws Exception {
        OperationValidator.checkMemberNotNull(null, new FakeException());
    }

    private static class FakeException extends RuntimeException {
    }

    @Test
    public void testValidateMembersFollowRfc6902Format_addWithoutValueFails() throws Exception {
        try {
            OperationValidator.validateMembersFollowRfc6902Format(addBuilder.value(null).build());
            fail("add operations must have a value member");
        } catch (OperationFormatException e) {
            assertEquals("value is required for add operations", e.getMessage());
        }
    }

    @Test
    public void testValidateMembersFollowRfc6902Format_addWithValueSucceeds() throws Exception {
        Operation expected = addBuilder.value(BooleanNode.FALSE).build();
        Operation result = OperationValidator.validateMembersFollowRfc6902Format(expected);
        assertEquals(expected, result);
    }

    @Test
    public void testValidateMembersFollowRfc6902Format_addWithFromFails() throws Exception {
        Operation operation = addBuilder.from(JsonPointer.compile("/from/here")).build();
        try {
            OperationValidator.validateMembersFollowRfc6902Format(operation);
            fail("providing a from member should cause an add operation to fail");
        } catch (OperationFormatException e) {
            assertEquals("value is required for add operations", e.getMessage());
        }
    }
}
