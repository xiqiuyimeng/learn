package com.demo.learn.util.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.Setter;

import java.io.Reader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


public class GsonUtil {
    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, new JsonDateSerializer())
            .registerTypeAdapter(Integer.class, new JsonIntegerSerializer())
            .registerTypeAdapter(BigDecimal.class, new JsonBigDecimalSerializer())
            .create();

    public static String toJson(Object obj){
        return gson.toJson(obj);
    }

    public static <T> T fromJson(String json, Class<T> t){
        return gson.fromJson(json, t);
    }

    public static <T> T fromJson(String json,Type typeOfT){
        return gson.fromJson(json,typeOfT);
    }

    public static <T> T fromJson(Reader reader, Type typeOfT){
        return gson.fromJson(reader,typeOfT);
    }

    public static void main(String[] args) {
        String str = "[{\"id\":1,\"name\":\"lwt\",\"time\":\"16.51\",\"date\":\"2020-05-09 16:48:31\"}," +
                "{\"id\":1,\"name\":\"lwt\",\"time\":\"16.51\",\"date\":\"1589014111000\"}]";
        List<User> u = fromJson(str, new TypeToken<List<User>>(){}.getType());
        System.out.println(toJson(u));
    }

    @Setter
    @Getter
    private static class User {
        private Integer id;
        private String name;
        private BigDecimal time;
        private Date date;

    }
}
