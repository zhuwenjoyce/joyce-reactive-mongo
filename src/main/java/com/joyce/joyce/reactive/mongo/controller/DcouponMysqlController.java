package com.joyce.joyce.reactive.mongo.controller;

import com.joyce.joyce.reactive.mongo.model.DcouponsMysqlModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;

/**
 * @author Joyce Zhu
 * @date 2021-11-07
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class DcouponMysqlController {

    final R2dbcEntityTemplate r2dbcEntityTemplate;

    @RequestMapping("/dcoupon/mysql/saveOne")
    public Mono<DcouponsMysqlModel> findByUsername() {
        DcouponsMysqlModel model = DcouponsMysqlModel.builder()
                .id(1L)
                .mongoId("1")
                .vid("vid1")
                .status("active")
                .swid("swid")
                .issuePlatform("wechat")
                .issueTimestamp(ZonedDateTime.now())
                .couponId("coupon-id")
                .swid("swid")
                .createTime(ZonedDateTime.now())
                .updateTime(ZonedDateTime.now())
                .build();
        return r2dbcEntityTemplate.insert(model);
    }

}
