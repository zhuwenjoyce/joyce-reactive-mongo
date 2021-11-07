package com.joyce.joyce.reactive.mongo.压测;

import com.joyce.joyce.reactive.mongo.model.DcouponsMysqlModel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.ZonedDateTime;

/**
 * @author Joyce Zhu
 * @date 2021-11-07
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class Mysql批量insert {

    @Autowired
    R2dbcEntityTemplate r2dbcEntityTemplate;

    @Test
    public void insertOne() throws InterruptedException {
        DcouponsMysqlModel model = DcouponsMysqlModel.builder()
                .id(3L)
                .mongoId(3)
                .vid("vid2")
                .status("active")
                .swid("swid")
                .issuePlatform("wechat")
                .issueTimestamp(ZonedDateTime.now())
                .couponId("coupon-id")
                .swid("swid")
                .createTime(ZonedDateTime.now())
                .updateTime(ZonedDateTime.now())
                .build();
        r2dbcEntityTemplate.insert(model)
                .doFinally(t -> {
                    log.error("doFinally!!! t = {}", t);
                    System.exit(1);  // 0是温和退出，1是暴力退出
                })
                .doOnError(throwable -> {
                    log.error("catch error!!!", throwable);
                    System.exit(1);
                })
                .subscribe();
        while (true) {
            // main thread must be active
        }
    }

}
