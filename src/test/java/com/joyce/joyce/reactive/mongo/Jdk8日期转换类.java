package com.joyce.joyce.reactive.mongo;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.*;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.Date;

/*
各种已定义好的日期格式常量formatter对象
DateTimeFormatter.ISO_OFFSET_DATE_TIME      2011-12-03
DateTimeFormatter.ISO_DATE                  2011-12-03
DateTimeFormatter.ISO_LOCAL_DATE            2011-12-03
DateTimeFormatter.ISO_LOCAL_TIME            10:15 or 10:15:30
DateTimeFormatter.ISO_LOCAL_DATE_TIME       2011-12-03T10:15:30

DateTimeFormatter.ISO_OFFSET_DATE           2011-12-03+01:00
DateTimeFormatter.ISO_OFFSET_DATE_TIME      2011-12-03T10:15:30+01:00 （推荐应用系统使用此格式）
DateTimeFormatter.ISO_OFFSET_TIME           10:15+01:00 or 10:15:30+01:00
DateTimeFormatter.ISO_TIME                  10:15, 10:15:30 or 10:15:30+01:00
DateTimeFormatter.ISO_ZONED_DATE_TIME       2011-12-03T10:15:30+01:00[Europe/Paris]
DateTimeFormatter.ISO_DATE_TIME             2011-12-03T10:15:30, 2011-12-03T10:15:30+01:00 or 2011-12-03T10:15:30+01:00[Europe/Paris]

DateTimeFormatter.ISO_ORDINAL_DATE          2012-337 (DAY_OF_YEAR)
DateTimeFormatter.ISO_WEEK_DATE             2012-W48-6 (WEEK_OF_WEEK_BASED_YEAR) (DAY_OF_WEEK)
DateTimeFormatter.ISO_INSTANT               2011-12-03T10:15:30Z
DateTimeFormatter.BASIC_ISO_DATE            20111203
DateTimeFormatter.RFC_1123_DATE_TIME        Tue, 3 Jun 2008 11:05:30 GMT
 */
/**
 * @author Joyce Zhu
 * @date 2021-11-06
 */
@Slf4j
public class Jdk8日期转换类 {


    /***********************************************
     *      各种日期转String
     ***********************************************
     */

    @Test
    public void LocalDate_to_String() {
        String str = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
        log.info("str = " + str); //2021-11-06
    }

    @Test
    public void LocalDateTime_to_String() {
        String str = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("str = " + str);  // 2021-11-06 21:51:31
    }

    @Test
    public void java_util_Date_to_String() {
        String str = LocalDateTime.ofInstant(new java.util.Date().toInstant(), ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        log.info("str = " + str);  //2021-11-06
    }

    @Test
    public void java_sql_Date_to_String() {
        String str = new java.sql.Date(System.currentTimeMillis()).toLocalDate()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        log.info("str = " + str);  //2021-11-06
    }

    @Test
    public void java_sql_Timestamp_to_String() {
        String str = new java.sql.Timestamp(System.currentTimeMillis()).toLocalDateTime()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("str = " + str);  //2021-11-06 21:56:03
    }

    @Test
    public void ZonedDateTime_to_String() {
        String str = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("str = " + str);  //2021-11-06 22:13:54
    }


    /***********************************************
     *      String转各种日期
     ***********************************************
     */

    @Test  // 特别经典，建议收藏
    public void String_to_ZonedDateTime() {
        ZonedDateTime dateTime = ZonedDateTime.from(DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.systemDefault())
                .parse("2011-05-24T14:15:30+08:00"));
        log.info("time = " + dateTime); //2011-05-24T14:15:30+08:00[Asia/Shanghai]
    }

    @Test
    public void String_to_LocalDate() {
        LocalDate date = LocalDate.parse("2020-04-13", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        log.info("date = " + date);  //2020-04-13
    }

    @Test
    public void String_to_LocalDateTime() {
        LocalDateTime time = LocalDateTime.parse("2020-04-13 18:49:30", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("time = " + time);  //2020-04-13T18:49:30
    }

    @Test
    public void String_to_java_util_Date() {
        Instant instant = LocalDate.parse("2020-04-13", DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                .atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant();
        java.util.Date date = java.util.Date.from(instant);
        log.info("date = " + date);  // Mon Apr 13 00:00:00 CST 2020
    }

    @Test
    public void String_to_java_sql_Date() {
        LocalDate localDate = LocalDate.parse("2020-04-20", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        java.sql.Date date = java.sql.Date.valueOf(localDate);
        log.info("date = " + date);  //2020-04-20
    }

    @Test
    public void String_to_java_sql_Timestamp() {  // String 转 java.sql.Date
        LocalDateTime localDateTime = LocalDateTime.parse("2020-04-20 19:20:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        java.sql.Timestamp timestamp = Timestamp.valueOf(localDateTime);
        log.info("timestamp = " + timestamp);  //2020-04-20 19:20:00.0
    }

    /***********************************************
     *      错误示例与正例
     ***********************************************
     */

    @Test
    public void LocalDate_to_String_错误示例_LocalDate不包含时分秒不能使用HHmmss() {
        try {
            String text = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (UnsupportedTemporalTypeException e) {
            log.error("错误示例: LocalDate不包含时分秒不能使用HH:mm:ss, 应该用LocalDateTime, " + e.getMessage(), e);
        }
    }

    @Test
    public void String_to_LocalDateTime_错误示例_LocalDateTime格式化必须包含时分秒HHmmss() {
        try {
            LocalDateTime parsedDate = LocalDateTime.parse("2020-04-13", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeException e) {
            log.error("LocalDateTime格式化必须包含时分秒HH:mm:ss, " + e.getMessage(), e);
        }
    }

    @Test
    public void LocalDateTime_to_String_正例_但是LocalDateTime可以使用格式yyyy_MM_dd() {
        String 日期string = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        log.info("LocalDateTime转String，日期string===" + 日期string);
    }

    /***********************************************
     *      ZonedDateTime日期类与其他各种日期类互转
     ***********************************************
     */

    @Test
    public void LocalDateTime_转_ZonedDateTime() {  // LocalDateTime 转 ZonedDateTime
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(
                LocalDateTime.of(2021, 5, 30, 7, 0, 0)
                        .atZone(ZoneId.systemDefault()).toInstant(),
                ZoneId.systemDefault()
        );
        LocalDateTime time = LocalDateTime.ofInstant(zonedDateTime.toInstant(), ZoneId.systemDefault());
        log.info("time = {}", time); //2021-05-30T07:00
    }

    @Test
    public void java_util_date_转_ZonedDateTime() {
        ZonedDateTime time = ZonedDateTime.ofInstant(
                new java.util.Date().toInstant(),
                ZoneId.systemDefault()
        );
        log.info("time = {}", time);  //2021-11-06T22:23:00.076+08:00[Asia/Shanghai]
    }

    @Test
    public void ZonedDateTime_转_java_util_date() {
        Date date = Date.from(ZonedDateTime.now().toInstant());
        log.info("date = {}", date);  //Sat Nov 06 22:45:15 CST 2021
    }

    @Test
    public void java_sql_date_转_ZonedDateTime() {
        ZonedDateTime time = ZonedDateTime.ofInstant(
                new java.sql.Date(System.currentTimeMillis()).toLocalDate().atStartOfDay().toInstant(ZoneOffset.UTC),
                ZoneId.systemDefault()
        );
        log.info("time = {}", time);  //2021-11-06T08:00+08:00[Asia/Shanghai]
    }

    /***********************************************
     *      其他各种日期类互转
     ***********************************************
     */

    @Test
    public void util_date_转_LocalDate() {
        LocalDate date = LocalDateTime.ofInstant(new java.util.Date().toInstant(), ZoneId.systemDefault())
                .toLocalDate();
        log.info("date = {}", date);  //2021-11-06
    }

    @Test
    public void LocalDate_转_java_util_date() {
        Instant instant = LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        java.util.Date date = java.util.Date.from(instant);
        log.info("date = {}", date);  //Sat Nov 06 00:00:00 CST 2021
    }

    @Test
    public void util_date_互转_LocalDateTime() {
        java.util.Date date = new java.util.Date();
        LocalDateTime time = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        log.info("time = {}", time);  //2021-11-06T22:34:11.358
    }

    @Test
    public void LocalDateTime_转_java_util_date() {
        Instant instant = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant();
        java.util.Date date = java.util.Date.from(instant);
        log.info("date = {}", date);   //Sat Nov 06 22:34:22 CST 2021
    }

    @Test
    public void java_util_date_转_java_sql_Timestamp() {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(new java.util.Date().toInstant(), ZoneId.systemDefault());
        java.sql.Timestamp timestamp = Timestamp.valueOf(localDateTime);
        log.info("timestamp = " + timestamp);  //2021-11-06 22:35:17.553
    }

    @Test
    public void java_sql_Timestamp_转_java_util_date() {
        LocalDateTime time = new java.sql.Timestamp(System.currentTimeMillis()).toLocalDateTime();
        log.info("time = " + time);   //2021-11-06T22:35:57.063
    }

    @Test
    public void java_util_date_转_LocalDate_和_LocalTime() {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(new java.util.Date().toInstant(), ZoneId.systemDefault());
        LocalTime localTime = localDateTime.toLocalTime();
        LocalDate localDate = localDateTime.toLocalDate();
        log.info("localDate = {}, localTime = {}", localDate, localTime);  //localDate = 2021-11-06, localTime = 22:38:06.205
    }

    @Test
    public void LocalDate_和_LocalTime_转_LocalDateTime() {
        LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.now());
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        java.util.Date date = java.util.Date.from(instant);
        log.info("date = {}", date);   //Sat Nov 06 22:40:09 CST 2021
    }


    @Test
    public void LocalTime_to_util_date() {
        // 只有时分秒的LocalTime建议不要转需要年月日的date
    }

}
