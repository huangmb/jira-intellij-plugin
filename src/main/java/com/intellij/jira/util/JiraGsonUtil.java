package com.intellij.jira.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.Collections;
import java.util.List;

import static java.util.Objects.nonNull;

public class JiraGsonUtil {


    public static JsonObject createNameObject(String value){
        return createObject("name", value);
    }

    public static JsonArray createArrayNameObject(String value){
        return createArrayNameObjects(Collections.singletonList(value));
    }

    public static JsonArray createArrayNameObjects(List<String>  values){
        JsonArray array = new JsonArray();
        JsonObject name = new JsonObject();
        for(String value : values){
            name.add("name", new JsonPrimitive(value));
        }

        array.add(name);
        return array;
    }

    public static JsonElement createNameObject(String value, boolean isArray){
        return isArray ? createArrayNameObject(value) : createNameObject(value);
    }

    public static JsonObject createIdObject(String value){
        return createObject("id", value);
    }

    public static JsonObject createObject(String property, String value){
        JsonObject name = new JsonObject();
        name.add(property, new JsonPrimitive(value));

        return name;
    }


    public static boolean isEmpty(JsonArray jsonArray){
        return nonNull(jsonArray) && jsonArray.size() == 0;
    }


}
