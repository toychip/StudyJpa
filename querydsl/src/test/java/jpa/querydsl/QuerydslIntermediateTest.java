package jpa.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpa.querydsl.dto.MemberDto;
import jpa.querydsl.dto.QMemberDto;
import jpa.querydsl.dto.UserDto;
import jpa.querydsl.entity.Member;
import jpa.querydsl.entity.QMember;
import jpa.querydsl.entity.Team;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static jpa.querydsl.entity.QMember.*;

@SpringBootTest
@Transactional
public class QuerydslIntermediateTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    JPAExpressions jpaExpressions;

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
    void simpleProjection() {
        List<String> result = queryFactory
                .select(member.username)
                .from(member)
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    @Test
    void tupleProjection() {
        List<Tuple> result = queryFactory
                .select(member.username, member.age)
                .from(member)
                .fetch();
        for (Tuple tuple : result) {
            String username = tuple.get(member.username);
            Integer age = tuple.get(member.age);
            System.out.println("username = " + username);
            System.out.println("age = " + age);
        }
    }

    @Test
    void findDtoByJPQL() {
        List<MemberDto> result = em.createQuery("select new jpa.querydsl.dto.MemberDto(m.username, m.age) " +
                                "from Member m",
                        MemberDto.class)
                .getResultList();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    void findDtoBySetter() {
        List<MemberDto> result = queryFactory
                .select(Projections.bean(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();
        // 기본 생성자에 값을 setter 해야하므로 기본 생성자 필요

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    void findDtoByField() {
        // 위와 다르게 getter, setter가 필요 없음
        List<MemberDto> result = queryFactory
                .select(Projections.fields(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();
        // 기본 생성자에 값을 setter 해야하므로 기본 생성자 필요

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    void findUserDtoByField() {
        // 프로퍼티나, 필드 접근 생성 방식에서 이름이 다를 때 해결 방안
//      ExpressionUtils.as (soutce,alias) : 필드나 서브 쿼리에 별칭 사용

        QMember memberSub = new QMember("memberSub");

        List<UserDto> result = queryFactory
                .select(Projections.fields(UserDto.class,
                        member.username.as("name"),
//                       다른 dto형태로 가져올때 필드의 이름이 다를 때 as로 alias해주기

                        ExpressionUtils.as(JPAExpressions
                                .select(memberSub.age.max())
                                .from(memberSub), "age")
                        ))
                .from(member)
                .fetch();

        for (UserDto userDto : result) {
            System.out.println("userDto = " + userDto);
        }

        /*
        userDto = UserDto(name=member1, age=40)
        userDto = UserDto(name=member2, age=40)
        userDto = UserDto(name=member3, age=40)
        userDto = UserDto(name=member4, age=40)
         */
    }

    @Test
    void findDtoByConstructor() {
        // 생성자를 활용하여 쿼리 날리면 dto가 달라져도 값의 타입만 맞으면 같기 때문에 생성자가 좋아보이지만
        // member.id를 select에서 조회하면 오류 발견됨
        List<UserDto> result = queryFactory
                .select(Projections.constructor(UserDto.class,
                        member.username,
                        member.age))
                // 생성자를 황용한 방법이므로 member~ 뒤 값의 type을 맞춰야함
                .from(member)
                .fetch();

        for (UserDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    void findDtoByQueryProjection() {
        // DTO에다가 추가해야하므로 의존성에 문제가 있을 수 있다.
        List<MemberDto> result = queryFactory
                .select(new QMemberDto(member.username, member.age)).distinct()
                .from(member)
                .where(member.username.eq("member1"))
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    void dynamicQuery_BooleanBuilder() {
        String usernameParam = "member1";
        Integer ageParam = 10;

        List<Member> result = searchMember1(usernameParam, ageParam);
        Assertions.assertThat(result.size()).isEqualTo(1);

    }
    private List<Member> searchMember1(String usernameCond, Integer ageCond) {
        BooleanBuilder builder = new BooleanBuilder();
        if(usernameCond != null){
            // 값이 존재하면 boolean에다가 and 조건 추가
            builder.and(member.username.eq(usernameCond));
        }

        if (ageCond != null) {
            builder.and(member.age.eq(ageCond));
        }

        return queryFactory
                .selectFrom(member)
                .where(builder)
                .fetch();
    }

    @Test
    void dynamicQuery_WHereParam() {
        String usernameParam = "member1";
        Integer ageParam = null;

        List<Member> result = searchMember2(usernameParam, ageParam);
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    private List<Member> searchMember2(String usernameCond, Integer ageCond) {
        return queryFactory
                .selectFrom(member)
//                .where(usernameEq(usernameCond), ageEq(ageCond))
//                반환되어 파라미터로 들어온 값이 null이면 무시가 됨
                .where(allEq(usernameCond, ageCond))
                .fetch();
    }

    private BooleanExpression usernameEq(String usernameCond) {
        return usernameCond != null ? member.username.eq(usernameCond) : null;
    }

    private BooleanExpression ageEq(Integer ageCond){
        return ageCond != null ? member.age.eq(ageCond) : null;
    }

    // 광고 상태 isServiceable, 날짜가 IN: isServicable 조립 가능 메서드로 나누어서 한 번에,
//    private BooleanExpression isServicable(String usernameCond, Integer ageCond) {
//        return isValid()
//    }

    private BooleanExpression allEq(String usernameCond, Integer ageCond){
        return usernameEq(usernameCond).and(ageEq(ageCond));

    }

// 쿼리 한 번에 대량 수정
// 번크연산.. ex) 모든 개발자의 연봉을 인상해

    @Test
    void bulkUpdate() {
//        회원의 나이가 28세 이하이면 비회원이라는 이름으로 바꿈
        //      영속성 컨텍스트    DB
        // member1 = 10 ->      비회원
        // member2 = 20 ->      비회원
        // member3 = 30 ->      member3
        // member4 = 40 ->      member4
//      바로 db에 값을 넣는 벌크 연산

        long count = queryFactory
                .update(member)
                .set(member.username, "비회원")
                .where(member.age.lt(28))
                .execute();
        em.flush();
        em.clear();

        List<Member> result = queryFactory
                .selectFrom(member)
                .fetch();
        for (Member member1 : result) {
            System.out.println("member1 = " + member1);
        }
    }

    @Test
    public void bulkAdd() {
        // 모든 회원의 나이를 1살 더하기 , 빼고 싶으면 add(-1)
//        곲하기는 multiply()
        long count = queryFactory
                .update(member)
                .set(member.age, member.age.add(1))
                .execute();
    }

    @Test
    void bulkDelete() {
//        18살 이상의 모든 회원을 지우는 Query
        long count = queryFactory
                .delete(member)
                .where(member.age.gt(18))
                .execute();
    }

}
