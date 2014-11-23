package org.samlikescode.eternity.json.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

/**
 * //todo(sb)
 */
public class JsonHandlerTest {

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
     * MODEL:
     *  {
     *
     *  }
     */
    @Test
    public void testTraversal() throws Exception {
        FakePojo fakePojo = new FakePojo("w7634534yr3432", true, 7476328749384l, new FakeInnerPojo("45353453534e", Lists.newArrayList("one", "doo", "three")));
        String incomingJson = "{\"id\":\"w7634534yr3432\",\"active\":true,\"birthday\":7476328749384,\"innerPojo\":{\"id\":\"45353453534e\",\"favorites\":[\"one\",\"doo\",\"three\"]}}";
        ValueNode node = JsonNodeFactory.instance.pojoNode(fakePojo);
        JsonNodeType nodeType = node.getNodeType();

    }

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

    public static class Model<T> {
        private final String name;
        private final ImmutableSet<T> fields;

        private Model(String name, ImmutableSet<T> fields) {
            this.name = name;
            this.fields = fields;
        }

        @JsonCreator
        public static <T> Model<T> of(@JsonProperty("name") String name, @JsonProperty("fields") ImmutableSet<T> fields) {
            return new Model<>(name, fields);
        }

        public String getName() {
            return name;
        }

        public ImmutableSet<T> getFields() {
            return fields;
        }
    }

    public static class Field {
        private final String field;
        private final String type;
        private final boolean required;

        @JsonCreator
        public Field(@JsonProperty("field") String field,
                     @JsonProperty("type") String type,
                     @JsonProperty("required") boolean required) {
            this.field = field;
            this.type = type;
            this.required = required;
        }

        public String getField() {
            return field;
        }

        public String getType() {
            return type;
        }

        public boolean isRequired() {
            return required;
        }
    }

    public static class ObjectField extends Field {
        private final ImmutableSet<Field> fields;

        @JsonCreator
        public ObjectField(@JsonProperty("field") String field,
                           @JsonProperty("type") String type,
                           @JsonProperty("required") boolean required,
                           @JsonProperty("fields") ImmutableSet<Field> fields) {
            super(field, "object", required);
            this.fields = fields;
        }

        public ImmutableSet<Field> getFields() {
            return fields;
        }
    }

    public static class CollectionField extends Field {
        private final Member member;

        @JsonCreator
        public CollectionField(@JsonProperty("field") String field,
                               @JsonProperty("type") String type,
                               @JsonProperty("required") boolean required,
                               @JsonProperty("member") Member member) {
            super(field, "collection", required);
            this.member = member;
        }

        public Member getMember() {
            return member;
        }

        private static class Member {
            private final String field;
            private final String regex;
            private final ImmutableSet<Field> fields;

            @JsonCreator
            private Member(@JsonProperty("field") String field,
                           @JsonProperty("regex") String regex,
                           @JsonProperty("fields") ImmutableSet<Field> fields) {
                this.field = field;
                this.regex = regex;
                this.fields = fields;
            }

            private String getField() {
                return field;
            }

            private String getRegex() {
                return regex;
            }

            private ImmutableSet<Field> getFields() {
                return fields;
            }
        }
    }
}
