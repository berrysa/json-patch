package org.samlikescode.http.support.jackson;

import com.fasterxml.jackson.core.JsonPointer;

import java.util.function.BiFunction;

/**
 * @author berrysa
 */
public final class JsonPointers {
    private JsonPointers() {}

    /**
     * Determines if one {@link JsonPointer} is a child of another.<br>
     *
     * Examples:
     * <ul>
     * <li> - true:  parent = "/a/b", child = "/a/b/c"
     * <li> - false: parent = "/a/b", child = "/cheese"
     * <li> - false: parent = "/a/b", child = "/a/b"
     * <li> - false: parent = "/a/b", child = "/a/e"
     * <li> - false: parent = "/a/b", child = "/a/babe"
     * </ul>
     */
    public static BiFunction<JsonPointer, JsonPointer, Boolean> IS_CHILD_POINTER =
            (parent, child) -> {
                String childPath = child.toString();
                String parentPath = parent.toString();
                return childPath.length() > parentPath.length()
                        && parentPath.equals(childPath.substring(0, parentPath.length()))
                        && childPath.charAt(parentPath.length()) == '/';
            };
}
