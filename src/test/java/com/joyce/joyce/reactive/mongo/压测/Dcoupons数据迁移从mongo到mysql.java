package com.joyce.joyce.reactive.mongo.压测;

import com.joyce.joyce.reactive.mongo.dao.DcouponsMongoRepository;
import com.joyce.joyce.reactive.mongo.model.DcouponsMongoModel;
import com.joyce.joyce.reactive.mongo.model.DcouponsMysqlModel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

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
    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    AtomicInteger totalMigrateCount = new AtomicInteger(0);

    @Test
    public void migrate() {
        String paramStartDate = "2016-11-09";
        ZonedDateTime migrateStartTime = ZonedDateTime.from(formatter.withZone(ZoneId.systemDefault()).parse(paramStartDate + " 00:00:00"));
        log.info("migrateStartTime = " + migrateStartTime); //2011-05-24T14:15:30+08:00[Asia/Shanghai]

        Long startMs = System.currentTimeMillis();
        migrateByCreateAtBetween(migrateStartTime)
                .doOnSuccess(dcouponsMysqlModel -> {
                    log.info("migrate data is success");
                })
                .doOnError(throwable -> {
                    log.error("migrate is error", throwable);
                })
                .doFinally(t -> {
                    Long endMs = System.currentTimeMillis();
                    log.info("migrate is finished in {} ms", endMs.longValue() - startMs.longValue());
                })
                .subscribe();
        while (true) {

        }
    }

    private Mono<DcouponsMysqlModel> migrateByCreateAtBetween(ZonedDateTime migrateStartTime) {
        if (migrateStartTime.compareTo(ZonedDateTime.now()) >= 0) {
            return Mono.just(DcouponsMysqlModel.builder().build());
        }

        ZonedDateTime migrateEndTime = migrateStartTime.plusHours(96);
        AtomicInteger currentMigrateCount = new AtomicInteger(0);

        return reactiveMongoTemplate.find(
                query(where("createAt").gte(migrateStartTime).lte(migrateEndTime)).with(Sort.by("createAt").ascending())
                        , DcouponsMongoModel.class, "dcoupons")
                .flatMap(m -> {
                    DcouponsMysqlModel dcouponsMysqlModel = DcouponsMysqlModel.builder().vid(m.getVid())
                            .swid(m.getSwid())
                            .couponId(m.getCouponId())
                            .status(m.getStatus())
                            .mongoId(m.get_id())
                            .validateStartTime(m.getValidateStartTime())
                            .validateEndTime(m.getValidateEndTime())
                            .issuePlatform(m.getIssuePlatform())
                            .issueTimestamp(m.getIssueTimestamp())
                            .createTime(m.getCreateAt())
                            .updateTime(m.getUpdateAt())
                            .build();
                    return r2dbcEntityTemplate.insert(dcouponsMysqlModel);
                })
                .doOnNext(dcouponsMysqlModel -> {
                    currentMigrateCount.addAndGet(1);
                })
                .defaultIfEmpty(DcouponsMysqlModel.builder().build())
                .last()
                .doOnNext(dcouponsMysqlModel -> {
                    log.info("current migrate count = {}, total migrate count = {}, dcoupon vid = {}, createAt between {} and {}"
                            , currentMigrateCount.get()
                            , totalMigrateCount.addAndGet(currentMigrateCount.get())
                            , dcouponsMysqlModel.getVid()
                            , migrateStartTime.format(formatter)
                            , migrateEndTime.format(formatter));
                })
                .flatMap(dcouponsMysqlModel -> migrateByCreateAtBetween(migrateEndTime));
    }


}
