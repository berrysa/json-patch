package org.samlikescode.infinity.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableSet;

/**
 * //todo(sb)
 */
public class Model {
    private final String name;
    private final ImmutableSet<Field> fields;

    private Model(String name, ImmutableSet<Field> fields) {
        this.name = name;
        this.fields = fields;
    }

    @JsonCreator
    public static Model of(@JsonProperty("name") String name,
                           @JsonProperty("fields") ImmutableSet<Field> fields) {
        return new Model(name, fields);
    }

    public String getName() {
        return name;
    }

    public ImmutableSet<? extends Field> getFields() {
        return fields;
    }
}
