package com.example.talent_man.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.Year;

public class YearDeserializer extends JsonDeserializer<Year> {
    @Override
    public Year deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        int value = jsonParser.getIntValue();
        return Year.of(value);
    }
}
