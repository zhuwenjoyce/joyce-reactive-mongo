package com.joyce.joyce.reactive.mongo.压测;

import com.joyce.joyce.reactive.mongo.dao.DcouponsRepository;
import com.joyce.joyce.reactive.mongo.model.DcouponsModel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Joyce Zhu
 * @date 2021-11-07
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class 批量insert {
    @Autowired
    DcouponsRepository dcouponsRepository;
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
        DcouponsModel dcoupon = DcouponsModel.builder()
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
        dcouponsRepository.save(dcoupon).subscribe();
    }

    //批量造数据，通过目测，大概一秒钟能insert 1万多条数据，也就是100万条数据insert到mongodb大概只需要100秒
    @Test
    public void batchInsert() throws InterruptedException {
        AtomicInteger incrementId = new AtomicInteger(1001018);
        List<DcouponsModel> modelList = new ArrayList<>();

        Flux.range(1, 200000)
                .flatMap(i -> dcouponsRepository.save(getDcouponModel(incrementId)))
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

    private DcouponsModel getDcouponModel(AtomicInteger incrementId) {
        String vid = "";
        for (int j = 0; j < 20; j++) {
            vid = vid + random.nextInt(10);
        }
        int id = incrementId.addAndGet(1);
        if(id % 5000 == 0) {
            log.info("id = {}", id);
        }
        ZonedDateTime issueTime = ZonedDateTime.now().minusDays(random.nextInt(1825));   // 随机减去过去五年1825天内，任意天数
        DcouponsModel dcoupon = DcouponsModel.builder()
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
    public void test() {

    }

}
