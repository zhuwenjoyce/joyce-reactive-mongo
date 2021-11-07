package com.joyce.joyce.reactive.mongo.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * such as '2011-12-03T10:15:30+01:00'
 * @author: Joyce Zhu
 * @date: 2020/10/30
 */
public class ZonedDateTimeOffsetSerialize extends JsonSerializer<ZonedDateTime> {

    @Override
    public void serialize(ZonedDateTime dateTime, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        jsonGenerator.writeString(dateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.systemDefault())));
    }
}
