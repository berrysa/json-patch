package org.samlikescode.databind;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;

public class CustomObjectMapper extends ObjectMapper {
    public CustomObjectMapper() {
        super();
        configure();
    }

    private void configure() {
        registerModule(new GuavaModule());
        registerModule(new JodaModule());
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        disable(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS);
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }
}
