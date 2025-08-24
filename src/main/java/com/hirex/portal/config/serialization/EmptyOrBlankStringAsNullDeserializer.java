package com.hirex.portal.config.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class EmptyOrBlankStringAsNullDeserializer extends StdDeserializer<String> {

    public EmptyOrBlankStringAsNullDeserializer(){
        super(String.class);
    }

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String result = StringDeserializer.instance.deserialize(jsonParser, deserializationContext);
        if (StringUtils.isBlank(result)) {
            return null;
        }
        return result.trim();
    }
}
