package jpa.querydsl.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jpa.querydsl.dto.MemberSearchCondition;
import jpa.querydsl.dto.MemberTeamDto;
import jpa.querydsl.entity.Member;
import jpa.querydsl.entity.Team;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

    @Test
    void searchTest() {
            Team teamA = new Team("teamA");
            Team teamB = new Team("teamB");
            em.persist(teamA);
            em.persist(teamB);
            Member member1 = new Member("member1", 10, teamA);
            Member member2 = new Member("member2", 20, teamA);
            Member member3 = new Member("member3", 30, teamB);
            Member member4 = new Member("member4", 40, teamB);
            em.persist(member1);
            em.persist(member2);
            em.persist(member3);
            em.persist(member4);

        MemberSearchCondition condition = new MemberSearchCondition();
        condition.setAgeGoe(35);
        condition.setAgeLoe(45);
        condition.setTeamName("teamB");
        List<MemberTeamDto> result = memberJpaRepository.searchByBuilder(condition);
        Assertions.assertThat(result).extracting("username").containsExactly("member4");

        MemberSearchCondition condition2 = new MemberSearchCondition();
        condition2.setTeamName("teamB");
        List<MemberTeamDto> result2 = memberJpaRepository.searchByBuilder(condition2);
        Assertions.assertThat(result2).extracting("username").containsExactly("member3","member4");

    }
}