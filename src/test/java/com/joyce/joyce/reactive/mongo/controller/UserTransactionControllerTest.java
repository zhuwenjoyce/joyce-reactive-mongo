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
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        Random random = new Random();
        List<UserModel> list = new ArrayList<>();
        list.add(UserModel.builder().username("user5")
                .money(new BigDecimal(500))
                .birthday(ZonedDateTime.now().minusDays(random.nextInt(3000)))
                .createTime(ZonedDateTime.now()).build());
        list.add(UserModel.builder().username("user6")
                .money(new BigDecimal(900))
                .birthday(ZonedDateTime.now().minusDays(random.nextInt(3000)))
                .createTime(ZonedDateTime.now()).build());

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

    }


    @Test
    public void deleteAll() throws Exception {
        StepVerifier.create(userRepository.deleteAll())
                .verifyComplete();
    }

}
