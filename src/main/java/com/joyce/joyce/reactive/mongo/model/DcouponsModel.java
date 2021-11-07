package com.joyce.joyce.reactive.mongo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;

/**
 * @author Joyce Zhu
 * @date 2021-11-07
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "dcoupons")
public class DcouponsModel {

    @Id
    private String _id;    //32位uuid全小写
    private Integer idint;
    private String status;   //active,pending,redeemed
    private String vid;      //19位数字
    private String couponId;  //36位UUID全小写，外面套个{}
    private String swid;
    private String issuePlatform;   //wechat
    private ZonedDateTime issueTimestamp;    //2021-09-17T10:15:20.239+00:00
    private ZonedDateTime validateStartTime;
    private ZonedDateTime validateEndTime;
    private ZonedDateTime createAt;
    private ZonedDateTime updateAt;

}
/*
例子
_id:605195eb9cfc04002ce97dba
status:"active"
vid:"2007487332000010068"
couponId:"DC20210225EPEP1"
swid:"{dc75a212-933e-465a-8dd0-91b3afdfe785}"
issuePlatform:"wechat"
issueTimestamp:2021-03-17T05:38:51.239+00:00
validateStartTime:2020-08-31T16:00:00.000+00:00
validateEndTime:2021-10-01T15:59:59.999+00:00
createdAt:2021-03-17T05:38:51.257+00:00
updatedAt:2021-04-08T01:32:23.003+00:00
__v:0



_id:605195eb9cfc04002ce97dbd
status:"active"
vid:"2007487332000010068"
couponId:"DC20210225EPEP1"
swid:"{dc75a212-933e-465a-8dd0-91b3afdfe785}"
issuePlatform:"wechat"
issueTimestamp:2021-03-17T05:38:51.239+00:00
validateStartTime:2020-08-31T16:00:00.000+00:00
validateEndTime:2021-10-01T15:59:59.999+00:00
createdAt:2021-03-17T05:38:51.257+00:00
updatedAt:2021-03-17T05:38:51.257+00:00
__v:0



*/