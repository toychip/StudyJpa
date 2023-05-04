package JPA.SpringDataJpa.repository;

import JPA.SpringDataJpa.dto.MemberDto;
import JPA.SpringDataJpa.entity.Member;
import JPA.SpringDataJpa.entity.Team;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
//@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("memberA");
        Member member2 = new Member("memberB");

        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건 조회 검증
        Member findmember1 = memberRepository.findById(member1.getId()).get();
        Member findmember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findmember1).isEqualTo(findmember1);
        assertThat(findmember2).isEqualTo(findmember2);

        // 리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        assertThat(count).isEqualTo(2);
    }

    @Test
    void findByUsernameAndAgegreaterThen() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> reuslt = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(reuslt.get(0).getUsername()).isEqualTo("AAA");
        assertThat(reuslt.get(0).getAge()).isEqualTo(20);
        assertThat(reuslt.size()).isEqualTo(1);
    }

    @Test
    void findHelloBy() {
        List<Member> helloBy = memberRepository.findTop3HelloBy();
    }

    @Test
    void testQuery() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> reuslt = memberRepository.findUser("AAA", 10);
        assertThat(reuslt.get(0)).isEqualTo(m1);
    }

    @Test
    void findUsernameList() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> usernameList = memberRepository.findUsernameList();
        for (String s : usernameList) {
            System.out.println("s = " + s);
        }
    }

    @Test
    void findMemberDto() {
        Team team = new Team("teamA");
        teamRepository.save(team);


        Member m1 = new Member("AAA", 10);
        m1.setTeam(team);
        memberRepository.save(m1);


        List<MemberDto> memberDto = memberRepository.findMemberDto();

        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }
    }
    @Test
    void findByNames() {
            Member m1 = new Member("AAA", 10);
            Member m2 = new Member("BBB", 20);

            memberRepository.save(m1);
            memberRepository.save(m2);

            List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));

        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    @Test
    void returnType() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findListByUsername("AAA");
        // 여기서 해당된 값이 만약에 없다면, null이 아니라 빈 Entity를 반환한다.
        // null이 아니므로 실무에서 자주 쓰임

        for (Member member : result) {
            System.out.println("member = " + member);
        }

        // 하지만 단건 조회에서 문제가 된다.
        // 여기서 매칭이 안되면 Null
        // 순수 JPA에서는 결과값이 없다면 NoResultEsception이 터짐,
        // Spring Data Jpa는 결과값이 없다면 감싸서 null로 반환하기 때문
        Member findMember = memberRepository.findMemberByUsername("AAA");
        System.out.println("findMember = " + findMember);

        // 있을 수도 있고 없을수도 있으면 Optional을 쓰는게 맞음.
        // 단건 조회지만 만약에 값이 2개라면, 오류가 터짐
        Optional<Member> aaa = memberRepository.findOptionalByUsername("AAA");
        System.out.println("aaa = " + aaa.get());
    }


}