package com.joyce.joyce.reactive.mongo.controller;

import com.joyce.joyce.reactive.mongo.dao.UserRepository;
import com.joyce.joyce.reactive.mongo.model.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Joyce Zhu
 * @date 2021-11-06
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTransactionControllerTest {

//    @Autowired
//    UserTransactionController userTransactionController;
//    @Autowired
//    TransactionalOperator transactionalOperator;
//    @Autowired
//    ReactiveTransactionManager reactiveTransactionManager;
    @Autowired
    UserRepository userRepository;

    @Test
    public void saveAll() throws Exception {
        List<UserModel> list = new ArrayList<>();
        list.add(UserModel.builder().username("user3").money(new BigDecimal(500)).build());
        list.add(UserModel.builder().username("user4").money(new BigDecimal(900)).build());

        StepVerifier.create(userRepository.saveAll(list))
                .expectNextCount(2)
                .verifyComplete();

//        userTransactionController.saveAll()
//                .doOnNext(userModel -> {
//                    log.info("userModel === ", userModel.getUsername());
//                })
//                .subscribe();
    }

    @Test
    public void saveMany() throws Exception {
        List<UserModel> list = new ArrayList<>();
        list.add(UserModel.builder().username("user3").money(new BigDecimal(500)).build());
        list.add(UserModel.builder().username("user4").money(new BigDecimal(900)).build());

    }


    @Test
    public void deleteAll() throws Exception {
        StepVerifier.create(userRepository.deleteAll())
                .verifyComplete();
    }

}
