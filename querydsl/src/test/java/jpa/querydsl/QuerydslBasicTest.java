package jpa.querydsl;


import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpa.querydsl.entity.Member;
import jpa.querydsl.entity.QMember;
import jpa.querydsl.entity.QTeam;
import jpa.querydsl.entity.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import java.util.List;

import static jpa.querydsl.entity.QMember.member;
import static jpa.querydsl.entity.QTeam.team;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {
    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @Test
    @DisplayName("메뉴얼")
    void test1(){
        //given
        Member findMember = queryFactory
                .selectFrom(member)
                .where(
                        member.username.eq("member1"), // username = 'member1'
                        member.username.ne("member1"), //username != 'member1'
                        member.username.eq("member1").not(), // username != 'member1'
                        member.username.isNotNull(), //이름이 is not null
                        member.age.in(10, 20), // age in (10,20)
                        member.age.notIn(10, 20), // age not in (10, 20)
                        member.age.between(10,30),//between 10, 30
                        member.age.goe(30), // age >= 30
                        member.age.gt(30),// age > 30
                        member.age.loe(30), // age <= 30
                        member.age.lt(30), // age < 30
                        member.username.like("member%"), //like 검색
                        member.username.contains("member"), // like ‘%member%’ 검색
                        member.username.startsWith("member") //like ‘member%’ 검색
                )
                .fetchOne();
    }


    @BeforeEach
    void before() {
        queryFactory = new JPAQueryFactory(em);
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
    }

    @Test
    void startJPQL() {
        // member1을 찾아라
        String qlString =
                "select m from Member m " +
                "where m.username = :username";
        Member findMember = em.createQuery(qlString, Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    void startQuerydsl() {
//        QMember m = new QMember("m");
//        QMember m = QMember.member;
//        QMember.member static import사용

        Member findMember = queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq("member1"))    // 파라미터 바인딩 처리
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    void search() {
        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1").and(member.age.eq(10)))
                .fetchOne();
        // 10살 & member1

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    void searchAndParam() {
        Member findMember = queryFactory
                .selectFrom(member)
                .where(
                        member.username.eq("member1"),
                        member.age.eq(10)
                )
                .fetchOne();
        // 10살 & member1

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    void resultFatch() {
//        List
//        List<Member> fetch = queryFactory
//                .selectFrom(member)
//                .fetch();
//
//        단건 조회
//        Member resultOne = queryFactory
//                .selectFrom(member)
//                .fetchOne();
//
//        Member limit1AndFetch = queryFactory
//                .selectFrom(member)
//                .fetchFirst();
        // 페이징에서 사용
        QueryResults<Member> results = queryFactory
                .selectFrom(member)
                .fetchResults();

        //count 쿼리로 변경
        long count = queryFactory
                .selectFrom(member)
                .fetchCount();
    }

    @Test
    void sort() {

    /**
     *회원 정렬 순서
     * 1. 회원 나이 내림차순(desc)
     * 2. 회원 이름 올림차순(asc)
     * 단 2에서 회원 이름이 없으면 마지막에 출력(nulls last)
     * */

        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(100))
                .orderBy(member.age.desc(), member.username.asc().nullsLast())
                .fetch();

        Member member5 = result.get(0);
        Member member6 = result.get(1);
        Member memberNull = result.get(2);
        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(memberNull.getUsername()).isNull();
    }

    @Test
    void paging1() {
        List<Member> result = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)  // 1개 스킵
                .limit(2)
                .fetch();

        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("전체 조회")
    void paging2() {
        QueryResults<Member> queryResults = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)  // 1개 스킵
                .limit(2)
                .fetchResults();

        assertThat(queryResults.getTotal()).isEqualTo(4);
        assertThat(queryResults.getLimit()).isEqualTo(2);
        assertThat(queryResults.getOffset()).isEqualTo(1);
        assertThat(queryResults.getResults().size()).isEqualTo(2);
    }

    @Test
    void aggregation() {

        List<Tuple> result = queryFactory
                .select(
                        member.count(),
                        member.age.sum(),
                        member.age.avg(),
                        member.age.max(),
                        member.age.min()
                )
                .from(member)
                .fetch();
        Tuple tuple = result.get(0);
        assertThat(tuple.get(member.count())).isEqualTo(4);
        assertThat(tuple.get(member.age.sum())).isEqualTo(100);
        assertThat(tuple.get(member.age.avg())).isEqualTo(25);
        assertThat(tuple.get(member.age.max())).isEqualTo(40);
        assertThat(tuple.get(member.age.min())).isEqualTo(10);

    }
    
    @Test
    public void group() throws Exception {
    /**
     * 팀의 이름과 각 팀의 평균 연령을 구해라.
     */
        List<Tuple> result = queryFactory.select(team.name, member.age.avg())
                .from(member)
                .join(member.team, team)
                .groupBy(team.name)
                .fetch();

        Tuple teamA = result.get(0);
        Tuple teamB = result.get(1);

        assertThat(teamA.get(team.name)).isEqualTo("teamA");
        assertThat(teamA.get(member.age.avg())).isEqualTo(15);   // (10+ 20) / 2

        assertThat(teamB.get(team.name)).isEqualTo("teamB");
        assertThat(teamB.get(member.age.avg())).isEqualTo(35);   // (30+ 40) / 2
    }

    @Test
    void join() {

        // 팀 a에 소속된 모든 회원을 찾아라
        // select * from Member join
        List<Member> result = queryFactory
                .selectFrom(member)
                .join(member.team, team)
                .where(team.name.eq("teamA"))
                .fetch();

        assertThat(result)
                .extracting("username")
                .containsExactly("member1", "member2");
    }

    /*
    회원 이름이 팀 이름과 같은 회원 조회
     */
    @Test
    void tehta_join() {
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC"));

        List<Member> theta_join = queryFactory
                .select(member)
                .from(member, team)
                .where(member.username.eq(team.name))
                .fetch();

        assertThat(theta_join)
                .extracting("username")
                .containsExactly("teamA", "teamB");
    }

    @Test
    void join_on_filtering() {
//      회원과 팀을 조인하면서, 팀 이름이 teamA인 팀만 조인, 회원은 모두 조회
//      JPQL:  select m, t from Member m left join m.team t on t.name = 'taemA'
        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(member.team, team).on(team.name.eq("teamA"))
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    /*
    연관관계가 없는 엔티티 외부 조인
    회원의 이름이 팀 이름과 같은 대상 외부 조인
     */
    @Test
    void join_on_no_relation() {
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC"));

        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(team)
//                id를 매칭하지 않기 때문에 이름으로만 join됨
//                이름이 같은 경우에만 team을 조인
                .on(member.username.eq(team.name))  // data 필터링, join하는 대상을 줄임
//                .where(member.username.eq(team.name))
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    @PersistenceUnit
    EntityManagerFactory emf;
    @Test
    void fetchJoinNo() {
        em.flush();
        em.clear();

//        fetch join 할때는 영속성 컨텍스트를 지워줘야 결과가 정상

        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1"))
                .fetchOne();
        // isLoaded -> 이미 로딩된(초기화) 엔티티인지 아닌지를 알려주는 함수
        // fetch join이 적용되지 않았을 때는 로딩이 되면 안됨.
        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).as("페치 조인 미적용").isFalse();
    }

    @Test
    void fetchJoinUse() {
        em.flush();
        em.clear();

//        fetch join 할때는 영속성 컨텍스트를 지워줘야 결과가 정상

        Member findMember = queryFactory
                .selectFrom(member)
                .join(member.team, team).fetchJoin()
                .where(member.username.eq("member1"))
                .fetchOne();
        // isLoaded -> 이미 로딩된(초기화) 엔티티인지 아닌지를 알려주는 함수
        // fetch join이 적용되지 않았을 때는 로딩이 되면 안됨.
        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).as("페치 조인 미적용").isTrue();
    }

    @Test
    void subQuery() {
        // 나이가 가장 많은 회원 조회
        // 앨리어스가 중복되는 경우에는 생성해서 사용
        QMember memberSub = new QMember("memberSub");


        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(
                        JPAExpressions
                                .select(memberSub.age.max())
                                .from(memberSub)
                ))
                .fetch();

        assertThat(result).extracting("age")
                .containsExactly(40);
    }

    @Test
    void subQueryGoe() {
        // 나이가 평균 이상인 회원 조회
        QMember memberSub = new QMember("memberSub");


        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.goe(
                        JPAExpressions
                                .select(memberSub.age.avg())
                                .from(memberSub)
                ))
                .fetch();

        assertThat(result).extracting("age")
                .containsExactly(30, 40);
    }

    @Test
    void subQueryIn() {
        // 나이가 10살 이상인 회원 조회
        QMember memberSub = new QMember("memberSub");


        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.in(
                        JPAExpressions
                                .select(memberSub.age)
                                .from(memberSub)
                                .where(memberSub.age.gt(10))
                ))
                .fetch();

        assertThat(result).extracting("age")
                .containsExactly(20, 30, 40);
    }

    @Test
    void selectSubQuery() {
        // 나이가 평균 이상인 회원 조회
        QMember memberSub = new QMember("memberSub");


        List<Tuple> result = queryFactory
                .select(member.username,
                        JPAExpressions
                                .select(memberSub.age.avg())
                                .from(memberSub))
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    @Test
    void basicCase() {
        List<String> result = queryFactory
                .select(member.age
                        .when(10).then("열살")
                        .when(20).then("스살살")
                        .otherwise("기타"))
                .from(member)
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    @Test
    void complexCase() {
        List<String> result = queryFactory
                .select(new CaseBuilder()
                        .when(member.age.between(0, 20)).then("0~20살")
                        .when(member.age.between(21, 30)).then("21살~30살")
                        .otherwise("기타")
                )
                .from(member)
                .fetch();
        for (String s : result) {
            System.out.println("s = " + s);
        }
    }
    
    @Test
    void constant(){
        // 조건에 맞을때 해당 조건에서 특정 값만 뽑는 것
        List<Tuple> result = queryFactory
                .select(member.username, Expressions.constant("A"))
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    @Test
    void concat() {
        //문자 더하기
        // username + _ + user.age
        List<String> result = queryFactory
                .select(member.username.concat("_").concat(member.age.stringValue()))
                .from(member)
                .where(member.username.eq("member1"))
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }
    }


}
