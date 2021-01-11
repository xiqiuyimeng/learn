package com.demo.learn.util.gson;

import com.google.gson.*;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;


public class JsonIntegerSerializer implements JsonSerializer<Integer>, JsonDeserializer<Integer> {

    @Override
    public Integer deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        String strText = json.getAsString();
        if (StringUtils.isBlank(strText)){
            return null;
        }
        return json.getAsInt();
    }

    @Override
    public JsonElement serialize(Integer integer, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(integer);
    }
}
