package org.samlikescode.http.jsonpatch.json;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.google.common.collect.Lists;
import org.samlikescode.http.jsonpatch.Operation;

import java.util.List;

/**
 * @author berrysa
 */
public class JsonPatchTestData {
    public static final String EXAMPLE_INSTRUCTION_ARRAY = "[\n" +
            "     { \"op\": \"test\", \"path\": \"/a/b/c\", \"value\": \"foo\" },\n" +
            "     { \"op\": \"remove\", \"path\": \"/a/b/c\" },\n" +
            "     { \"op\": \"add\", \"path\": \"/a/b/c\", \"value\": [ \"foo\", \"bar\" ] },\n" +
            "     { \"op\": \"replace\", \"path\": \"/a/b/c\", \"value\": 42 },\n" +
            "     { \"op\": \"move\", \"from\": \"/a/b/c\", \"path\": \"/a/b/d\" },\n" +
            "     { \"op\": \"copy\", \"from\": \"/a/b/d\", \"path\": \"/a/b/e\" }\n" +
            "   ]";

    public static final String MEDIA_TYPE = "application/json-patch+json";

    public static final List<String> EQUAL_OPERATIONS_DIFFERENT_PROPERTY_ORDER = Lists.newArrayList(
            "{ \"op\": \"add\", \"path\": \"/a/b/c\", \"value\": \"foo\" }",
            "{ \"path\": \"/a/b/c\", \"op\": \"add\", \"value\": \"foo\" }",
            "{ \"value\": \"foo\", \"path\": \"/a/b/c\", \"op\": \"add\" }");

    public static final Operation COMPLETE_OPERATION = Operation
            .builder(Operation.Op.REMOVE, JsonPointer.compile("/magic/path"))
            .from(JsonPointer.compile("/the/other/magic/path"))
            .value(BooleanNode.FALSE)
            .build();
}
