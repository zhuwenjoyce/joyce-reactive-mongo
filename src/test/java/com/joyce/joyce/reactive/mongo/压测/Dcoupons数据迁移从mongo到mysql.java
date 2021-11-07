package com.joyce.joyce.reactive.mongo.压测;

import com.joyce.joyce.reactive.mongo.dao.DcouponsMongoRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Joyce Zhu
 * @date 2021-11-07
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class Dcoupons数据迁移从mongo到mysql {

    @Autowired
    DcouponsMongoRepository dcouponsMongoRepository;
    @Autowired
    R2dbcEntityTemplate r2dbcEntityTemplate;

    @Test
    public void migrateOne() {

    }
}
