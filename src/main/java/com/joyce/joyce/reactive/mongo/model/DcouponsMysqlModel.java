package com.joyce.joyce.reactive.mongo.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.joyce.joyce.reactive.mongo.util.ZonedDateTimeOffsetDeserializer;
import com.joyce.joyce.reactive.mongo.util.ZonedDateTimeOffsetSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.time.ZonedDateTime;

/**
 * @author Joyce Zhu
 * @date 2021-11-07
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@org.springframework.data.relational.core.mapping.Table("t_dcoupon") // as r2dbc model
public class DcouponsMysqlModel {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    //自增主键

    @Column(name = "mongo_id")
    private String mongoId;
    private String status;   //active,pending,redeemed
    private String vid;      //19位数字
    @Column(name = "coupon_id")
    private String couponId;  //36位UUID全小写，外面套个{}
    private String swid;
    @Column(name = "issue_platform")
    private String issuePlatform;   //wechat

    @Column(name = "issue_timestamp")
    @JsonDeserialize(using = ZonedDateTimeOffsetDeserializer.class)
    @JsonSerialize(using = ZonedDateTimeOffsetSerialize.class)
    private ZonedDateTime issueTimestamp;    //2021-09-17T10:15:20.239+00:00

    @Column(name = "validate_start_time")
    @JsonDeserialize(using = ZonedDateTimeOffsetDeserializer.class)
    @JsonSerialize(using = ZonedDateTimeOffsetSerialize.class)
    private ZonedDateTime validateStartTime;

    @Column(name = "validate_end_time")
    @JsonDeserialize(using = ZonedDateTimeOffsetDeserializer.class)
    @JsonSerialize(using = ZonedDateTimeOffsetSerialize.class)
    private ZonedDateTime validateEndTime;

    @Column(name = "create_time")
    @JsonDeserialize(using = ZonedDateTimeOffsetDeserializer.class)
    @JsonSerialize(using = ZonedDateTimeOffsetSerialize.class)
    private ZonedDateTime createTime;

    @Column(name = "update_time")
    @JsonDeserialize(using = ZonedDateTimeOffsetDeserializer.class)
    @JsonSerialize(using = ZonedDateTimeOffsetSerialize.class)
    private ZonedDateTime updateTime;

}