package com.joyce.joyce.reactive.mongo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * r2dbc官方地址
 * https://spring.io/projects/spring-data-r2dbc
 * 对于底层的数据源来说，MongoDB, Redis, 和 Cassandra 可以直接以reactive的方式支持Spring Data。而其他很多关系型数据库比如Postgres, Microsoft SQL Server, MySQL, H2 和 Google Spanner 则可以通过使用R2DBC 来实现对reactive的支持。
 * 所以本项目不使用r2dbc，而直接使用reactive方式，也就是reactiveRepository方式
 *
 * 重点：看test测试类
 */
@EnableWebFlux
@EnableReactiveMongoRepositories
@EnableR2dbcRepositories
@SpringBootApplication
public class JoyceReactiveMongoApplication {

	public static void main(String[] args) {
		SpringApplication.run(JoyceReactiveMongoApplication.class, args);
	}

}
