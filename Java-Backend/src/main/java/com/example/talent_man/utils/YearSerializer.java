package com.example.talent_man.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.Year;

public class YearSerializer extends StdSerializer<Year> {

    public YearSerializer() {
        this(null);
    }

    public YearSerializer(Class<Year> t) {
        super(t);
    }

    @Override
    public void serialize(Year year, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("value", year.getValue());
        jsonGenerator.writeBooleanField("leap", year.isLeap());
        jsonGenerator.writeEndObject();
    }
}
