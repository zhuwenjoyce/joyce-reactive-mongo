package com.joyce.joyce.reactive.mongo.controller;

import com.joyce.joyce.reactive.mongo.dao.UserRepository;
import com.joyce.joyce.reactive.mongo.model.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
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
@EnableTransactionManagement // it can support the transaction demos
public class UserTransactionControllerTest {

    @Autowired
    UserTransactionController userTransactionController;
    @Autowired
    TransactionalOperator transactionalOperator;
    @Autowired
    ReactiveTransactionManager reactiveTransactionManager;
    @Autowired
    UserRepository userRepository;
    Random random = new Random();

    @Test
    public void saveAll() {
        List<UserModel> list = new ArrayList<>();
        list.add(UserModel.builder().username("user5")
                .money(new BigDecimal(500))
                .birthday(ZonedDateTime.now().minusDays(random.nextInt(3000)))
                .createTime(ZonedDateTime.now()).build());
        list.add(UserModel.builder().username("user6")
                .money(new BigDecimal(900))
                .birthday(ZonedDateTime.now().minusDays(random.nextInt(3000)))
                .createTime(ZonedDateTime.now()).build());

        StepVerifier
                .create(userRepository.saveAll(list))
                .expectNextCount(2)  //  How many data are inserted
                .verifyComplete();

        StepVerifier
                .create(userRepository.findAll())
                .expectNextCount(2)  //  How many data are founded
                .verifyComplete();

//        StepVerifier.create(userRepository.saveAll("1","2"))
//                .expectError()  //  if save another type entity which should be error
//                .verify();

        StepVerifier
                .create(userRepository.findAll())
                .expectNextCount(2)  //  当上一个step发生错误时，应该没有插入成功插入数据，所以此时查出的数据应该还是两条。
                .verifyComplete();

        StepVerifier
                .create(userRepository.deleteAll())
                .expectNextCount(2)  //  因为数据库只有两条，所以此时delete all就是2
                .verifyComplete();

        StepVerifier
                .create(userRepository.findAll())
                .expectNextCount(0)  //  上一个step delete all，所以此时数据库里是0
                .verifyComplete();


    }

    @Transactional
    @Test
    public void flux事务例子1() {  // 需要在类上加注解：@EnableTransactionManagement 或者在方法上加注解：@Transactional 建议使用方法注解，可以缩小事务范围
        List<UserModel> list = new ArrayList<>();
        list.add(UserModel.builder().username("user5")
                .money(new BigDecimal(500))
                .birthday(ZonedDateTime.now().minusDays(random.nextInt(3000)))
                .createTime(ZonedDateTime.now()).build());

        Flux<UserModel> flux = Flux.fromStream(list.stream())
                .flatMap(userRepository::save)
                .doOnNext(userModel -> {
                    Assert.isTrue(StringUtils.isNotBlank(userModel.getId()), "id should not be null if save successfully");
                });
        transactionalOperator.execute(status ->flux);
    }

    @Transactional
    @Test
    public void flux事务例子2(){  // 需要在类上加注解：@EnableTransactionManagement  或者在方法上加注解：@Transactional 建议使用方法注解，可以缩小事务范围
        List<UserModel> list = new ArrayList<>();
        list.add(UserModel.builder().username("user5")
                .money(new BigDecimal(500))
                .birthday(ZonedDateTime.now().minusDays(random.nextInt(3000)))
                .createTime(ZonedDateTime.now()).build());

        Flux<UserModel> flux = Flux.fromStream(list.stream())
                .flatMap(userRepository::save)
                .doOnNext(userModel -> {
                    Assert.isTrue(StringUtils.isNotBlank(userModel.getId()), "id should not be null if save successfully");
                })
                .as(transactionalOperator::transactional);
    }


    @Test
    public void deleteAll() throws Exception {
        StepVerifier.create(userRepository.deleteAll())
                .verifyComplete();
    }

}
