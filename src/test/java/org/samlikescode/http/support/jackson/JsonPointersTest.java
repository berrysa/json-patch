package org.samlikescode.http.support.jackson;

import com.fasterxml.jackson.core.JsonPointer;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author berrysa
 */
public class JsonPointersTest {
    @Test
    public void testIsChildPointer_isChild() throws Exception {
        JsonPointer parent = JsonPointer.compile("/a/b");
        JsonPointer child = JsonPointer.compile("/a/b/c");
        assertTrue(JsonPointers.IS_CHILD_POINTER.apply(parent, child));
    }

    @Test
    public void testIsChildPointer_cheese() throws Exception {
        JsonPointer parent = JsonPointer.compile("/a/b");
        JsonPointer child = JsonPointer.compile("/cheese");
        assertFalse(JsonPointers.IS_CHILD_POINTER.apply(parent, child));
    }

    @Test
    public void testIsChildPointer_equalChild() throws Exception {
        JsonPointer parent = JsonPointer.compile("/a/b");
        JsonPointer child = JsonPointer.compile("/a/b");
        assertFalse(JsonPointers.IS_CHILD_POINTER.apply(parent, child));
    }

    @Test
    public void testIsChildPointer_differentChild() throws Exception {
        JsonPointer parent = JsonPointer.compile("/a/b");
        JsonPointer child = JsonPointer.compile("/a/e");
        assertFalse(JsonPointers.IS_CHILD_POINTER.apply(parent, child));
    }

    @Test
    public void testIsChildPointer_differentChildSameStart() throws Exception {
        JsonPointer parent = JsonPointer.compile("/a/b");
        JsonPointer child = JsonPointer.compile("/a/babe");
        assertFalse(JsonPointers.IS_CHILD_POINTER.apply(parent, child));
    }
}
