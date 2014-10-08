package org.samlikescode.sandbox;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import org.joda.time.Instant;

import java.util.UUID;

public class SandboxObject {
    private final UUID id;
    private final Instant timestamp;
    private final boolean flag;
    private final ImmutableMap<Integer, String> map;

    @JsonCreator
    public SandboxObject(@JsonProperty("id") UUID id,
                         @JsonProperty("timestamp") Instant timestamp,
                         @JsonProperty("flag") boolean flag,
                         @JsonProperty("map") ImmutableMap<Integer, String> map) {
        this.id = id;
        this.timestamp = timestamp;
        this.flag = flag;
        this.map = map;
    }

    public UUID getId() {
        return id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public boolean isFlag() {
        return flag;
    }

    public ImmutableMap<Integer, String> getMap() {
        return map;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(
                id,
                timestamp,
                flag,
                map);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SandboxObject that = (SandboxObject) o;
        return Objects.equal(this.id, that.id)
                && Objects.equal(this.timestamp, that.timestamp)
                && Objects.equal(this.flag, that.flag)
                && Objects.equal(this.map, that.map);
    }
}
