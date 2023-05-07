package jpa.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jpa.querydsl.entity.QTestEntity;
import jpa.querydsl.entity.TestEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class QuerydslApplicationTests {
	@Autowired
	EntityManager em;
	@Test
	void contextLoads() {
		TestEntity testEntity = new TestEntity();
		em.persist(testEntity);
		JPAQueryFactory query = new JPAQueryFactory(em);
		QTestEntity qTestEntity = new QTestEntity("h");

		TestEntity result = query
				.selectFrom(qTestEntity)
				.fetchOne();
		assertThat(result).isEqualTo(testEntity);
		assertThat(result.getId()).isEqualTo(testEntity.getId());

	}
}
