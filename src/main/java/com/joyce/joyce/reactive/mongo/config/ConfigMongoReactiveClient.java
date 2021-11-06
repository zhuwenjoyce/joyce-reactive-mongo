package com.joyce.joyce.reactive.mongo.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;

import java.util.Arrays;
import java.util.List;

/**
 * @author Joyce Zhu
 * @date 2021-10-28
 */
@Slf4j
//@Configuration
public class ConfigMongoReactiveClient extends AbstractReactiveMongoConfiguration {

    @Bean
    public MongoClient mongoClient() {
//        WriteConcern wc = WriteConcern.W1.withJournal(true);
        WriteConcern wc = WriteConcern.ACKNOWLEDGED.withJournal(true);

        //单机配置
//        MongoClientOptions mco = MongoClientOptions.builder()
//                .writeConcern(wc)
//                .connectionsPerHost(100)
//                .readPreference(ReadPreference.secondary())
//                .threadsAllowedToBlockForConnectionMultiplier(5)
//                .readPreference(ReadPreference.secondaryPreferred())
//                .maxWaitTime(120000).connectTimeout(10000).build();
        //        MongoClient client = new MongoClient(asList, mco);

        List<ServerAddress> serverList = Arrays.asList(new ServerAddress("127.0.0.1", 27017));

        MongoClientSettings settings = MongoClientSettings.builder()
                .writeConcern(wc)
                .readConcern(ReadConcern.DEFAULT)
                .build();
        MongoClient client = MongoClients.create(settings);
        return client;
    }

    @Override
    protected String getDatabaseName() {
        return "lison";
    }

}
