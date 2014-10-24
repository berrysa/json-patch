package org.samlikescode.http.jsonpatch.operation;

import com.fasterxml.jackson.core.JsonPointer;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.samlikescode.http.jsonpatch.Operation;
import org.samlikescode.http.jsonpatch.OperationFormatException;
import org.samlikescode.http.jsonpatch.OperationValidator;

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
}
