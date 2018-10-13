package com.intellij.jira.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import static java.util.Objects.nonNull;

public class JiraGsonUtil {


    public static JsonObject createCommentObject(String value){
        JsonObject comment = new JsonObject();
        JsonArray operations = new JsonArray();
        JsonObject addOperation = new JsonObject();
        JsonObject bodyComment = new JsonObject();
        bodyComment.add("body", new JsonPrimitive(value));
        addOperation.add("add", bodyComment);
        operations.add(addOperation);
        comment.add("comment", operations);

        return comment;
    }

    public static JsonObject createNameObject(String value){
        JsonObject name = new JsonObject();
        name.add("name", new JsonPrimitive(value));

        return name;
    }

    public static JsonArray createArrayNameObject(String value){
        JsonArray array = new JsonArray();
        JsonObject name = new JsonObject();
        name.add("name", new JsonPrimitive(value));

        array.add(name);
        return array;
    }

    public static JsonElement createNameObject(String value, boolean isArray){
        return isArray ? createArrayNameObject(value) : createNameObject(value);
    }

    public static JsonObject createIdObject(String value){
        JsonObject name = new JsonObject();
        name.add("id", new JsonPrimitive(value));

        return name;
    }


    public static boolean isEmpty(JsonArray jsonArray){
        return nonNull(jsonArray) && jsonArray.size() == 0;
    }


}
