package com.cachat.prj.echo3.outils;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

public class LowercaseEnumSerializer implements JsonSerializer<Enum<?>> {
    @Override
    public JsonElement serialize(Enum<?> src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.name().toLowerCase());
    }
}