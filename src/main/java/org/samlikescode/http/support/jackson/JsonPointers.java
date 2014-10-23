package org.samlikescode.http.support.jackson;

import com.fasterxml.jackson.core.JsonPointer;

/**
 * @author berrysa
 */
public final class JsonPointers {
    private JsonPointers() {}

    public static boolean isChildPointer(JsonPointer parent, JsonPointer child) {
        String childPath = child.toString();
        String parentPath = parent.toString();
        return childPath.length() > parentPath.length()
                && parentPath.equals(childPath.substring(0, parentPath.length()));
    }
}
