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
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.DateOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.sql.In;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
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
    AtomicInteger count = new AtomicInteger(0);

    @Test
    public void migrate() {
        String startDate = "2016-11-09";

        ZonedDateTime migrateStartTime = ZonedDateTime.from(formatter.withZone(ZoneId.systemDefault()).parse(startDate + " 00:00:00"));
        log.info("migrateStartTime = " + migrateStartTime); //2011-05-24T14:15:30+08:00[Asia/Shanghai]

        migrateAll(migrateStartTime);
        while (true) {

        }
    }

    private void migrateAll(ZonedDateTime migrateStartTime) {
        ZonedDateTime migrateEndTime = migrateStartTime.plusDays(1000);

        Aggregation aggregation =  newAggregation(
                match(where("createAt").gte(migrateStartTime).lte(migrateEndTime)),
//                project("useCode","price","orderTime")
//                        .and(DateOperators.DateToString.dateOf("orderTime").toString("%m")).as("month"),
                group("_id").count().as("idint")
//                ,sort(Sort.by(Sort.Direction.ASC,"_id"))
        );

        int ii = reactiveMongoTemplate.aggregate(aggregation, "aggregation", Integer.class).defaultIfEmpty(0).blockLast();
        log.info("count = {}", ii);
//                .doOnNext(model -> {
//                    log.info("model == {}", model);
//                })
//                .doOnSubscribe(subscription -> {
//                    log.info("start...");
//                })
//                .doOnError(throwable -> {
//                    log.error("catch error.", throwable);
//                })
//                .subscribe();

//        Query query = query(where("createAt").gte(migrateStartTime).lte(migrateEndTime)).with(Sort.by("createAt").ascending());
//        reactiveMongoTemplate.find(query, DcouponsMongoModel.class, "dcoupons")
//                .flatMap(m -> {
//                    log.info("model = {}, count = {}", m.getCreateAt().format(formatter), count.addAndGet(1));
//                    DcouponsMysqlModel dcouponsMysqlModel = DcouponsMysqlModel.builder().vid(m.getVid())
//                            .swid(m.getSwid())
//                            .couponId(m.getCouponId())
//                            .status(m.getStatus())
//                            .mongoId(m.get_id())
//                            .validateStartTime(m.getValidateStartTime())
//                            .validateEndTime(m.getValidateEndTime())
//                            .issuePlatform(m.getIssuePlatform())
//                            .issueTimestamp(m.getIssueTimestamp())
//                            .createTime(m.getCreateAt())
//                            .updateTime(m.getUpdateAt())
//                            .build();
//                    return r2dbcEntityTemplate.insert(dcouponsMysqlModel);
//                })
//                .doOnNext(dcouponsMysqlModel -> {
//                    log.info("num {}, migrate id = {}, createAt = {}", count.addAndGet(1), dcouponsMysqlModel.getMongoId(), dcouponsMysqlModel.getCreateTime().format(formatter));
//                })
//                .last()
//                .map(dcouponsMysqlModel -> {
//
//                })
//                .subscribe()
//        ;
    }


}
