package com.joyce.joyce.reactive.mongo.config;

import com.joyce.joyce.reactive.mongo.util.ZonedDateTimeReadConverter;
import com.joyce.joyce.reactive.mongo.util.ZonedDateTimeWriteConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.Arrays;

/**
 * @author Joyce Zhu
 * @date 2021-11-06
 */
@Configuration
@RequiredArgsConstructor
public class ConfigMongoConverter {

    final ZonedDateTimeReadConverter zonedDateTimeReadConverter;
    final ZonedDateTimeWriteConverter zonedDateTimeWriteConverter;

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(Arrays.asList(zonedDateTimeReadConverter,zonedDateTimeWriteConverter));
    }

}
