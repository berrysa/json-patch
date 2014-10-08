package org.samlikescode.sandbox;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.joda.time.Instant;
import org.samlikescode.http.JsonPatchDocument;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.WILDCARD;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;

@RequestMapping(
        value = "sandboxes",
        consumes = APPLICATION_JSON,
        produces = APPLICATION_JSON
)
@RestController
public class Sandboxes {
    private final ImmutableMap.Builder<UUID, SandboxObject> database = populateDefaultData();

    @RequestMapping(method = GET, consumes = WILDCARD)
    public ImmutableList<SandboxObject> getAllSandboxObjects() {
        return database.build().values().asList();
    }

    @RequestMapping(value = "/{sandboxId}", method = GET, consumes = WILDCARD)
    public SandboxObject getSandboxObject(@PathVariable UUID sandboxId) {
        ImmutableMap<UUID, SandboxObject> db = database.build();
        if (db.containsKey(sandboxId)) {
            return db.get(sandboxId);
        }
        return null;
    }

    @RequestMapping(value = "/{sandboxId}", method = PATCH, consumes = APPLICATION_JSON)
    public SandboxObject patchSandboxObject(@PathVariable UUID sandboxId, JsonPatchDocument document) {
        ImmutableMap<UUID, SandboxObject> db = database.build();

        if (db.containsKey(sandboxId)) {
            SandboxObject priorRepresentation = db.get(sandboxId);

            ValueNode node = JsonNodeFactory.instance.pojoNode(priorRepresentation);
            JsonNode endpointToPatch = node.at(document.getPath());

            JsonNodeType nodeType = endpointToPatch.getNodeType();

            // todo: should be able to conjure up something cool here

            return priorRepresentation;
        }
        return null;
    }

    private static ImmutableMap.Builder<UUID, SandboxObject> populateDefaultData() {
        ImmutableMap.Builder<UUID, SandboxObject> builder = ImmutableMap.builder();
        UUID id1 = UUID.fromString("61381d60-869b-4129-9442-a073672a7165");
        UUID id2 = UUID.fromString("c7c22549-8f20-440f-a495-d513cf5d855f");
        UUID id3 = UUID.fromString("7eb83fab-5e15-4bb2-9fae-9e96c4ea6f38");
        UUID id4 = UUID.fromString("a3cd46d7-406a-4fb6-925e-f9fe1bace8a9");
        UUID id5 = UUID.fromString("19b46bf9-56a6-48ef-aaa3-31f293e77c03");
        return builder.put(id1, new SandboxObject(id1, new Instant(5), true, ImmutableMap.of(2, "yaaay")))
                .put(id2, new SandboxObject(id2, new Instant(443), false, ImmutableMap.of(45, "this")))
                .put(id3, new SandboxObject(id3, new Instant(92), true, ImmutableMap.of(222, "is")))
                .put(id4, new SandboxObject(id4, new Instant(231), false, ImmutableMap.of(34, "fuuuuun!")))
                .put(id5, new SandboxObject(id5, new Instant(1), true, ImmutableMap.of(777, ":-D")));
    }
}
