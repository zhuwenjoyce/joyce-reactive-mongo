package com.joyce.joyce.reactive.mongo.dao;

import com.joyce.joyce.reactive.mongo.model.UserModel;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Joyce Zhu
 * @date 2021-11-06
 */
@Repository
public interface UserRepository extends ReactiveMongoRepository<UserModel, Long> {

}
