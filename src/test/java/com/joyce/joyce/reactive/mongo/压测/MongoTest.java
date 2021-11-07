package com.joyce.joyce.reactive.mongo.压测;

import com.joyce.joyce.reactive.mongo.dao.DcouponsMongoRepository;
import com.joyce.joyce.reactive.mongo.model.DcouponsMongoModel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
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
public class MongoTest {
    @Autowired
    DcouponsMongoRepository dcouponsMongoRepository;
    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    final Random random = new Random();
    String[] couponIdArr = {"DC20210225EPEP1","DC20210225EPEP2","DC20210225EPEP3","DC20210225EPEP4","DC20210225EPEP5",
            "DC20210225EPEP6","DC20210225EPEP7","DC20210225EPEP8","DC20210225EPEP9","DC20210225EPEP10",
            "DC20210225EPEP11","DC20210225EPEP12","DC20210225EPEP13","DC20210225EPEP14","DC20210225EPEP15"};
    int couponIdArrSize = couponIdArr.length;


    @Test
    public void insertOne() {
        AtomicInteger incrementId = new AtomicInteger(0);
        String vid = "";
        for (int j = 0; j < 20; j++) {
            vid = vid + random.nextInt(10);
        }
        ZonedDateTime issueTime = ZonedDateTime.now().minusDays(random.nextInt(1825));   // 随机减去过去五年1825天内，任意天数
        DcouponsMongoModel dcoupon = DcouponsMongoModel.builder()
                ._id(incrementId.addAndGet(1) + "")
                .status(random.nextInt(100) > 60?"active":"redeemed")
                .vid(vid)
                .couponId(couponIdArr[random.nextInt(couponIdArrSize)])
                .swid("{" + UUID.randomUUID().toString().toLowerCase() + "}")
                .issuePlatform("wechat")
                .issueTimestamp(issueTime)
                .validateStartTime(issueTime.plusDays(1))
                .validateEndTime(issueTime.plusDays(random.nextInt(365))) // 一年内随机有效天数
                .createAt(issueTime)
                .updateAt(issueTime)
                .build();
        dcouponsMongoRepository.save(dcoupon).subscribe();
    }

    //批量造数据，通过目测，大概一秒钟能insert 1万多条数据，也就是100万条数据insert到mongodb大概只需要100秒
    @Test
    public void batchInsert() throws InterruptedException {
        AtomicInteger incrementId = new AtomicInteger(1001018);
        List<DcouponsMongoModel> modelList = new ArrayList<>();

        Flux.range(1, 200000)
                .flatMap(i -> dcouponsMongoRepository.save(getDcouponModel(incrementId)))
                .last()
                .doFinally(t -> {
                    log.error("doFinally!!! t = {}", t);
                    System.exit(1);  // 0是温和退出，1是暴力退出
                })
                .doOnError(throwable -> {
                    log.error("catch error!!!", throwable);
                    System.exit(1);
                })
                .subscribe()
                ;
        while (true) {
            // main thread must be active
        }
    }

    private DcouponsMongoModel getDcouponModel(AtomicInteger incrementId) {
        String vid = "";
        for (int j = 0; j < 20; j++) {
            vid = vid + random.nextInt(10);
        }
        int id = incrementId.addAndGet(1);
        if(id % 5000 == 0) {
            log.info("id = {}", id);
        }
        ZonedDateTime issueTime = ZonedDateTime.now().minusDays(random.nextInt(1825));   // 随机减去过去五年1825天内，任意天数
        DcouponsMongoModel dcoupon = DcouponsMongoModel.builder()
                ._id(id + "")
                .idint(id)
                .status(random.nextInt(100) > 60?"active":"redeemed")
                .vid(vid)
                .couponId(couponIdArr[random.nextInt(couponIdArrSize)])
                .swid("{" + UUID.randomUUID().toString().toLowerCase() + "}")
                .issuePlatform("wechat")
                .issueTimestamp(issueTime)
                .validateStartTime(issueTime.plusDays(1))
                .validateEndTime(issueTime.plusDays(random.nextInt(365))) // 一年内随机有效天数
                .createAt(issueTime)
                .updateAt(issueTime)
                .build();
        return dcoupon;
    }


    @Test
    public void minCreateAtInMongo() {
        Aggregation aggs = newAggregation(
//                match(where("orderTime").lt(commentDate)),
//                unwind("createAt"),
                group("createAt").min("createAt").as("createAt"),
                sort(Sort.by(Sort.Direction.ASC, "createAt")),
                limit(1L)
        );

        reactiveMongoTemplate.aggregate(aggs, "dcoupons", DcouponsMongoModel.class)
                .doOnNext(dcouponsMongoModel -> {
                    log.info("model 1 = {}", dcouponsMongoModel);
                    log.info("min create at = {}", dcouponsMongoModel.getCreateAt().format(DateTimeFormatter.ISO_OFFSET_DATE));
                })
                .subscribe();
        while (true) {
            // main thread must be active
        }
    }

    @Test
    public void maxCreateAtInMongo() {
        Aggregation aggs = newAggregation(
//                match(where("orderTime").lt(commentDate)),
//                unwind("createAt"),
                group("createAt").min("createAt").as("createAt"),
                sort(Sort.by(Sort.Direction.DESC, "createAt")),
                limit(1L)
        );

        reactiveMongoTemplate.aggregate(aggs, "dcoupons", DcouponsMongoModel.class)
                .doOnNext(dcouponsMongoModel -> {
                    log.info("model 1 = {}", dcouponsMongoModel);
                    log.info("max create at = {}", dcouponsMongoModel.getCreateAt().format(DateTimeFormatter.ISO_OFFSET_DATE));
                })
                .subscribe();
        while (true) {
            // main thread must be active
        }
    }

    /**
     * select * from dcoupons where createAt > ? and createAt < ?
     */
    @Test
    public void findAllBetweenCreateAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String gtDateStr = "2016-11-09";
        ZonedDateTime dateTime = ZonedDateTime.from(formatter.withZone(ZoneId.systemDefault()).parse(gtDateStr + " 00:00:00"));
        log.info("time = " + dateTime); //2011-05-24T14:15:30+08:00[Asia/Shanghai]

        Query query = query(where("createAt").gte(dateTime).lte(dateTime.plusDays(1L))).with(Sort.by("createAt").ascending());
//                .addCriteria(where("createAt").lt(dateTime.plusDays(1L)))

        AtomicInteger count = new AtomicInteger(0);
        reactiveMongoTemplate.find(query, DcouponsMongoModel.class, "dcoupons")
                .map(model -> {
                    log.info("model = {}, count = {}", model.getCreateAt().format(formatter), count.addAndGet(1));
                    return model;
                })
                .subscribe()
        ;
        while (true) {

        }
    }

    /**
     * select count(*) from dcoupons where createAt > ? and createAt < ?
     */
    @Test
    public void count_when_createAt_between() {
        String startDate = "2016-11-09";
        ZonedDateTime migrateStartTime = ZonedDateTime.from(formatter.withZone(ZoneId.systemDefault()).parse(startDate + " 00:00:00"));
        log.info("migrateStartTime = " + migrateStartTime); //2011-05-24T14:15:30+08:00[Asia/Shanghai]
        ZonedDateTime migrateEndTime = migrateStartTime.plusDays(1);

        Aggregation aggregation = newAggregation(
                match(where("createAt").gte(migrateStartTime).lte(migrateEndTime)),
//                project("useCode","price","orderTime")
//                        .and(DateOperators.DateToString.dateOf("orderTime").toString("%m")).as("month"),
                group().count().as("idint")
//                ,sort(Sort.by(Sort.Direction.ASC,"_id"))
        );

        DcouponsMongoModel ii = reactiveMongoTemplate.aggregate(aggregation, "dcoupons", DcouponsMongoModel.class)
                .map(model -> {
                    log.info("遍历model = {}", model);
                    return model;
                })
//                .defaultIfEmpty(0)
                .blockLast();
        log.info("count = {}", ii);

//        Integer countInt = reactiveMongoTemplate.aggregate(aggregation, "dcoupons", Integer.class)
////                .defaultIfEmpty(0)
//                .blockLast();
//        log.info("countInt = {}", countInt);
    }



    @Test
    public void test() {

    }

}
