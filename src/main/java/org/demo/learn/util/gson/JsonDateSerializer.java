package org.demo.learn.util.gson;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Pattern;


public class JsonDateSerializer implements JsonSerializer<Date>, JsonDeserializer<Date> {

    private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 正则：匹配yyyy-MM-dd HH:mm:ss
    private static final String TIME_PATTERN_DASH_LINE = "[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2]\\d|3[0-1])\\s([0-1]\\d|2[0-4]):([0-5]\\d):([0-5]\\d)";

    // 正则：匹配时间戳，到毫秒
    private static final String TIMESTAMP_PATTERN = "1\\d{12}";

    @Override
    public Date deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        String dateText = json.getAsString();
        LocalDateTime ldt = null;
        if (matchPattern(TIME_PATTERN_DASH_LINE, dateText)) {
            ldt = LocalDateTime.parse(dateText, df);
        } else if (matchPattern(TIMESTAMP_PATTERN, dateText)) {
            ldt = LocalDateTime.ofEpochSecond(Long.parseLong(dateText) / 1000, 0, ZoneOffset.ofHours(8));
        } else {
            throw new RuntimeException("日期格式不支持，无法转化");
        }
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public JsonElement serialize(Date date, Type type, JsonSerializationContext context) {
        LocalDateTime time = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        String dateText = df.format(time);
        return new JsonPrimitive(dateText);
    }

    private boolean matchPattern(String pattern, String targetStr) {
        return Pattern.compile(pattern).matcher(targetStr).matches();
    }
}
