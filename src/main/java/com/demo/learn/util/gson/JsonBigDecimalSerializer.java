package com.demo.learn.util.gson;

import com.google.gson.*;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.math.BigDecimal;


public class JsonBigDecimalSerializer implements JsonSerializer<BigDecimal>, JsonDeserializer<BigDecimal> {
    @Override
    public BigDecimal deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        String strText = json.getAsString();
        if (StringUtils.isBlank(strText)){
            return null;
        }
        return new BigDecimal(strText.trim());
    }

    @Override
    public JsonElement serialize(BigDecimal bigDecimal, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(bigDecimal);
    }
}
