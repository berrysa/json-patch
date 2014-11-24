package org.samlikescode.infinity.util.json.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.samlikescode.http.config.jackson.databind.CustomObjectMapper;

import java.io.File;
import java.util.List;

/**
 * //todo(sb)
 */
public class JsonHandlerTest {
    private ObjectMapper objectMapper = CustomObjectMapper.INSTANCE;

    /**
     * INCOMING:
     *  {
     *      "id":"w7634534yr3432",
     *      "active":true,
     *      "birthday":7476328749384,
     *      "innerPojo":{
     *          "id":"45353453534e",
     *          "favorites":["one","doo","three"]
     *      }
     *  }
     *
     * MODEL: see fakeProfileModel.json
     */
    @Test
    public void testTraversal() throws Exception {
        FakePojo fakePojo = new FakePojo("w7634534yr3432", true, 7476328749384l, new FakeInnerPojo("45353453534e", Lists.newArrayList("one", "doo", "three")));
        String incomingJson = "{\"id\":\"w7634534yr3432\",\"active\":true,\"birthday\":7476328749384,\"innerPojo\":{\"id\":\"45353453534e\",\"favorites\":[\"one\",\"doo\",\"three\"]}}";
        ValueNode node = JsonNodeFactory.instance.pojoNode(fakePojo);
        JsonNodeType nodeType = node.getNodeType();

    }

    @Test
    public void testBillyBob() throws Exception {
        JsonNode requestNode = objectMapper.readValue(new File("fakeProfileRequest_billyBob.json"), JsonNode.class);
        JsonNode modelNode = objectMapper.readValue(new File("fakeProfileModel.json"), JsonNode.class);

        JsonParser jsonParser = requestNode.traverse();
        JsonToken token = jsonParser.nextToken();
        if (token == null) {
            return;
        } else {

        }
    }

    //    @Test
//    public void testModel() throws Exception {
//        ImmutableSet<? extends Field> fields = ImmutableSet.of(
//                new ObjectField("first", true, null),
//                new CollectionField("favorites", true, new CollectionField.Member("rating", null, null)));
//        assertEquals(2, fields.size());
//    }

    public static class FakePojo {
        private String id;
        private Boolean active;
        private long birthday;
        private FakeInnerPojo innerPojo;

        public FakePojo() {}

        public FakePojo(String id, Boolean active, long birthday, FakeInnerPojo innerPojo) {
            this.id = id;
            this.active = active;
            this.birthday = birthday;
            this.innerPojo = innerPojo;
        }

        public String getId() {
            return id;
        }

        public Boolean getActive() {
            return active;
        }

        public long getBirthday() {
            return birthday;
        }

        public FakeInnerPojo getInnerPojo() {
            return innerPojo;
        }
    }

    public static class FakeInnerPojo {
        private String id;
        private List<String> favorites;

        public FakeInnerPojo() {}

        public FakeInnerPojo(String id, List<String> favorites) {
            this.id = id;
            this.favorites = favorites;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<String> getFavorites() {
            return favorites;
        }

        public void setFavorites(List<String> favorites) {
            this.favorites = favorites;
        }
    }
}
