package org.samlikescode.http;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.joda.time.Instant;
import org.junit.Before;
import org.junit.Test;
import org.samlikescode.databind.CustomObjectMapper;
import org.samlikescode.sandbox.SandboxObject;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class JsonPatchDocumentTest {
    private static final SandboxObject SANDBOX_OBJECT = new SandboxObject(UUID.fromString("a4d7ab5b-c7ae-4fd5-8db7-66df46e1fc4d"), new Instant(99), true, ImmutableMap.of(5, "five"));
    private ObjectMapper om;

    @Before
    public void setUp() throws Exception {
        om = new CustomObjectMapper();
    }

    @Test
    public void testDeserialization() throws Exception {
        JsonPatchDocument<SandboxObject> patchDocument = JsonPatchDocument.add(SANDBOX_OBJECT, JsonPointer.compile("/parent/child/grandchild"));
        assertEquals(SANDBOX_OBJECT, patchDocument.getValue().get());
    }

    @Test
    public void testDeserialization_fromJsonString() throws Exception {
        String valueJson = "{\"id\":\"a4d7ab5b-c7ae-4fd5-8db7-66df46e1fc4d\",\"timestamp\":\"1970-01-01T00:00:00.099Z\",\"flag\":true,\"map\":{\"5\":\"five\"}}";
        String json = "{\"op\":\"add\", \"path\": \"/a/b/c\", \"value\":" + valueJson + "}";
        JsonPatchDocument<SandboxObject> patchDocument = JsonPatchDocument.add(SANDBOX_OBJECT, JsonPointer.compile("/a/b/c"));
        JsonPatchDocument<SandboxObject> deserializedResult = om.readValue(json, new TypeReference<JsonPatchDocument<SandboxObject>>() {});
        assertEquals(patchDocument, deserializedResult);
    }
}
