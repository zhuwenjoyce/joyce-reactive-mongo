package com.joyce.joyce.reactive.mongo.dao;

import com.joyce.joyce.reactive.mongo.model.DcouponsModel;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Joyce Zhu
 * @date 2021-11-07
 */
@Repository
public interface DcouponsRepository extends ReactiveMongoRepository<DcouponsModel, String> {
}
