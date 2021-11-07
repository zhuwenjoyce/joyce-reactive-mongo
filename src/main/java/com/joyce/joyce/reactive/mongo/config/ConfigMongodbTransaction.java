package com.joyce.joyce.reactive.mongo.config;

import com.mongodb.internal.operation.TransactionOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.ReactiveMongoTransactionManager;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;

import javax.annotation.Resource;

/**
 * @author Joyce Zhu
 * @date 2021-11-06
 */
@Configuration
public class ConfigMongodbTransaction {

    @Bean
    TransactionalOperator transactionOperation(ReactiveTransactionManager rtm) {
        return TransactionalOperator.create(rtm);
    }

    @Bean
    public ReactiveTransactionManager transactionManager(ReactiveMongoDatabaseFactory dbf) {
        return new ReactiveMongoTransactionManager(dbf);
    }

}
