package jpa.querydsl.repository;

import jpa.querydsl.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {
    // 순수 JPA Test
    @Autowired
    EntityManager em;
    @Autowired MemberJpaRepository memberJpaRepository;

    @Test
    void basicTest() {
        Member member = new Member("member1", 10);
        memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.findById(member.getId()).get();
        Assertions.assertThat(findMember).isEqualTo(member);

        List<Member> all = memberJpaRepository.findAll();
        Assertions.assertThat(all).containsExactly(member);

        List<Member> result = memberJpaRepository.findByUsename("member1");
        Assertions.assertThat(result).containsExactly(member);
    }

    @Test
    void basicQuerydslTest() {
        Member member = new Member("member1", 10);
        memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.findById(member.getId()).get();
        Assertions.assertThat(findMember).isEqualTo(member);

        List<Member> all = memberJpaRepository.findAllQueryDsl();
        Assertions.assertThat(all).containsExactly(member);

        List<Member> result = memberJpaRepository.findByUsenameQueryDsl("member1");
        Assertions.assertThat(result).containsExactly(member);
    }
}