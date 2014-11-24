package org.samlikescode.infinity.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.samlikescode.http.GuavaCollectors;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * //todo(sb)
 */
public class Field {
    private final String field;
    private final Type type;
    private final boolean required;
    private final String regex;
    private final ImmutableSet<Field> fields;
    private final Member member;

    private Field(String field, Type type, Boolean required, String regex, ImmutableSet<Field> fields, Member member) {
        this.field = field;
        this.type = type == null ? Type.STRING : type;
        this.required = required == null ? false : required;
        this.regex = regex;
        this.fields = fields == null ? ImmutableSet.of() : fields;
        this.member = member;
    }

    @JsonCreator
    public static Field newFieldWithIgnoredArgumentsRemoved(@JsonProperty("field") String field,
                                                            @JsonProperty("type") Type type,
                                                            @JsonProperty("required") Boolean required,
                                                            @JsonProperty("regex") String regex,
                                                            @JsonProperty("fields") ImmutableSet<Field> fields,
                                                            @JsonProperty("member") Member member) {
        return removeIgnoredArguments.apply(new Field(field, type, required, regex, fields, member));
    }

    public static Field newField(String field, Type type, Boolean required, String regex, ImmutableSet<Field> fields, Member member) {
        return new Field(field, type, required, regex, fields, member);
    }

    public String getField() {
        return field;
    }

    public Type getType() {
        return type;
    }

    public boolean isRequired() {
        return required;
    }

    public String getRegex() {
        return regex;
    }

    public ImmutableSet<Field> getFields() {
        return fields;
    }

    public Member getMember() {
        return member;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String field;
        private Type type;
        private boolean required;
        private String regex;
        private ImmutableSet.Builder<Field> fieldBuilder = ImmutableSet.builder();
        private Member member;

        public Builder setField(String field) {
            this.field = field;
            return this;
        }

        public Builder setType(Type type) {
            this.type = type;
            return this;
        }

        public Builder setRequired(boolean required) {
            this.required = required;
            return this;
        }

        public Builder setRegex(String regex) {
            this.regex = regex;
            return this;
        }

        @JsonProperty("fields")
        public Builder addChildFields(Iterable<Field> fields) {
            fieldBuilder.addAll(fields);
            return this;
        }

        public Builder setMember(Member member) {
            this.member = member;
            return this;
        }

        public Field buildWithIgnoredArgumentsRemoved() {
            return Field.newFieldWithIgnoredArgumentsRemoved(field, type, required, regex, fieldBuilder.build(), member);
        }

        public Field build() {
            return Field.newField(field, type, required, regex, fieldBuilder.build(), member);
        }
    }

    public static class Member {
        private final String field;
        private final String regex;
        private final ImmutableSet<Field> fields;

        @JsonCreator
        public Member(@JsonProperty("field") String field,
                      @JsonProperty("regex") String regex,
                      @JsonProperty("fields") ImmutableSet<Field> fields) {
            this.field = field;
            this.regex = regex;
            this.fields = fields;
        }

        public String getField() {
            return field;
        }

        public String getRegex() {
            return regex;
        }

        public ImmutableSet<Field> getFields() {
            return fields;
        }
    }

    public enum Type {
        STRING("string"),
        COLLECTION("collection"),
        OBJECT("object"),
        BOOLEAN("boolean"),
        NUMBER("number");

        private static final ImmutableMap<String, Type> VALUE_MAP = Arrays.stream(Type.values())
                .collect(GuavaCollectors.toImmutableMap(Type::getValue, type -> type));

        private final String value;

        private Type(String value) {
            this.value = value;
        }

        @JsonCreator
        public static Type fromString(String input) {
            return VALUE_MAP.getOrDefault(input, null);
        }

        @JsonValue
        public String getValue() {
            return value;
        }
    }

    private static final Function<Field, Builder> copyBaseFieldsToBuilder = field -> Field.builder().setRequired(field.required).setType(field.type).setField(field.field);

    private static final ImmutableMap<Type, UnaryOperator<Field>> argumentRemoversByType = ImmutableMap.<Type, UnaryOperator<Field>>builder()
            .put(Type.STRING, field -> copyBaseFieldsToBuilder.apply(field).setRegex(field.regex).build())
            .put(Type.OBJECT, field -> copyBaseFieldsToBuilder.apply(field).addChildFields(field.fields).build())
            .put(Type.COLLECTION, field -> copyBaseFieldsToBuilder.apply(field).setMember(field.member).build())
            .put(Type.BOOLEAN, field -> copyBaseFieldsToBuilder.apply(field).build())
            .put(Type.NUMBER, field -> copyBaseFieldsToBuilder.apply(field).build())
            .build();

    private static final UnaryOperator<Field> removeIgnoredArguments = field -> argumentRemoversByType.get(field.type).apply(field);
}
