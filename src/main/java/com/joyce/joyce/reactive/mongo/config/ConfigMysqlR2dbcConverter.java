package com.joyce.joyce.reactive.mongo.config;

import com.joyce.joyce.reactive.mongo.util.LocalDateTimeToZonedDateTimeConverter;
import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 *  datetime custom converter solution
 * @author: Joyce Zhu
 * @date: 2020/11/20
 */
@Slf4j
@Configuration
public class ConfigMysqlR2dbcConverter extends AbstractR2dbcConfiguration {
    @Override
    public ConnectionFactory connectionFactory() {
        return null;
    }

    @Autowired
    LocalDateTimeToZonedDateTimeConverter timeConverter;

    @Override
    protected List<Object> getCustomConverters() {
        List converterList = new ArrayList();
        converterList.add(timeConverter);
//        converterList.add(new org.springframework.data.r2dbc.test.PersonWriteConverter());
        return converterList;
    }
}
