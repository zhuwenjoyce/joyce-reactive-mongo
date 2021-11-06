package com.joyce.joyce.reactive.mongo.controller;

import com.joyce.joyce.reactive.mongo.dao.UserRepository;
import com.joyce.joyce.reactive.mongo.model.UserModel;
import com.mongodb.reactivestreams.client.MongoClient;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Joyce Zhu
 * @date 2021-11-06
 */
//@Controller
public class UserTransactionController {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(UserTransactionController.class);
    final MongoClient mongoClient;
    final UserRepository userRepository;
    final TransactionalOperator transactionalOperator;
    final ReactiveTransactionManager reactiveTransactionManager;

    public UserTransactionController(MongoClient mongoClient, UserRepository userRepository, TransactionalOperator transactionalOperator, ReactiveTransactionManager reactiveTransactionManager) {
        this.mongoClient = mongoClient;
        this.userRepository = userRepository;
        this.transactionalOperator = transactionalOperator;
        this.reactiveTransactionManager = reactiveTransactionManager;
    }

    @RequestMapping("/user/findAll")
    public Flux<UserModel> findByUsername() {
        Map<String, Object> map = new HashMap<>();
        return userRepository.findAll();
    }

    @RequestMapping("/user/saveAll")
    public Flux<UserModel> saveAll() {
        List<UserModel> list = new ArrayList<>();
        list.add(UserModel.builder().username("user1").money(new BigDecimal(2000)).build());
        list.add(UserModel.builder().username("user2").money(new BigDecimal(800)).build());

        return transactionalOperator.execute(status ->
                Flux.fromStream(list.stream())
                .flatMap(userModel -> userRepository.save(userModel)).last()
        );
    }

}
