package com.joyce.joyce.reactive.mongo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * @author Joyce Zhu
 * @date 2021-10-28
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class UserModel {
    @Id
    private String id;
    private String username;
    private BigDecimal money;
    private ZonedDateTime birthday;
    private ZonedDateTime createTime;
}