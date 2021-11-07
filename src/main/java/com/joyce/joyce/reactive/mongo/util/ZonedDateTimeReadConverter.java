package com.joyce.joyce.reactive.mongo.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * @author Joyce Zhu
 * @date 2021-11-06
 */
@Component
public class ZonedDateTimeReadConverter implements Converter<Date, ZonedDateTime> {

    private final ZoneId zoneId = ZoneId.of("UTC+8");

    @Override
    public ZonedDateTime convert(Date date) {
        return date.toInstant().atZone(zoneId);
    }
}