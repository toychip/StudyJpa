package JPA.SpringDataJpa.repository;

import JPA.SpringDataJpa.dto.MemberDto;
import JPA.SpringDataJpa.entity.Member;
import JPA.SpringDataJpa.entity.Team;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;
    @Autowired MemberQueryRepository memberQueryRepository; // test이므로 @Autowired 원래는 생성자 주입으로 해야함

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

    @Test
    void paging() {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));
        memberRepository.save(new Member("member6", 10));

        int age=10;
        // 0페이지에서 3개 갖고 와   Spring Jpa는 페이지 index가 0부터 시작, 한 페이지의 개수는 3개
        // 여기서 페이지의 최대 개수나 최소 개수를 정하지 않았음. 임의로 설정 후 데이터를 갖고 온 것.
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.Direction.DESC, "username");

        //when
        Page<Member> page = memberRepository.findByAge(age, pageRequest);
        //total count query까지 같이 날림
        //api로 반환시 매우 위험, 안좋음

        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));
        //이건 api로 반환 가능

        //then                  // getContent : 내부에 있는 실제 데이터들을 꺼내고 싶을 때, 0번째 페이지에 3개를 가져옴
        List<Member> content = page.getContent();
        for (Member member : content) {
            System.out.println("member-- = " + member);
        }
        long totalElements = page.getTotalElements();// getTotalElements -> totalCount와 같은 것이다.
        System.out.println("totalElements = " + totalElements);

        assertThat(content.size()).isEqualTo(3);    // data를 3개 가져왔으므로
        assertThat(page.getTotalElements()).isEqualTo(6);   // 총 뎅이터는 6개
        assertThat(page.getNumber()).isEqualTo(0);  // 현재 페이지 번호 !! 0부터 시작하므로 당연히 0
        assertThat(page.getTotalPages()).isEqualTo(2); // 총 페이지 개수 6/3 = 2
        assertThat(page.isFirst()).isTrue();    // 당연히 첫번째 페이지이므로 True
        assertThat(page.hasNext()).isTrue();    // 다음 페이지가 있냐?
    }

    @Test
    void pagingSlicing() { // 지금 slice 적용 안된 상태. repository에 반환타입을 page로 설정해
//        slice는 더보기와 같은 역할을 함, 3개를 가져오라 하면 +1을 해서 가져옴
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));
        memberRepository.save(new Member("member6", 10));

        int age=10;
        // 0페이지에서 3개 갖고 와   Spring Jpa는 페이지 index가 0부터 시작
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.Direction.DESC, "username");

        //when
        Slice<Member> page = memberRepository.findByAge(age, pageRequest);
        //total count query까지 같이 날림

        //then                  // getContent : 내부에 있는 실제 데이터들을 꺼내고 싶을 때, 0번째 페이지에 3개를 가져옴
        List<Member> content = page.getContent();
        for (Member member : content) {
            System.out.println("member-- = " + member);
        }

        assertThat(content.size()).isEqualTo(3);    // data를 3개 가져왔으므로
        assertThat(page.getNumber()).isEqualTo(0);  // 현재 페이지 번호 !! 0부터 시작하므로 당연히 0
        assertThat(page.isFirst()).isTrue();    // 당연히 첫번째 페이지이므로 True
        assertThat(page.hasNext()).isTrue();    // 다음 페이지가 있냐?
    }

    @Test
    public void bulkUpdate() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 20));
        memberRepository.save(new Member("member3", 30));
        memberRepository.save(new Member("member4", 40));
        memberRepository.save(new Member("member5", 50));
        memberRepository.save(new Member("member6", 60));
        memberRepository.save(new Member("member7", 70));

        int resultCount = memberRepository.bulkAgePlus(20);
//        em.flush(); 변경되지 않은 것 db에 반영
//        @Modefying에서 clearAutomatically = true하면 생략 가능
//        em.clear(); // 벌크 연산 후에는 꼭 영속성 컨텍스트를 날려야함

        List<Member> result = memberRepository.findListByUsername("member5");
        Member member5 = result.get(0);
        System.out.println("member5 = " + member5);

        assertThat(resultCount).isEqualTo(6);
    }

    @Test
    public void findMemberLazy() {
        //given

        //member1 -> teamA
        //member2 -> teamA 각각 참조

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamA");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        //when
        List<Member> members = memberRepository.findMemberFetchJoin();

        for (Member member : members) {
            System.out.println("member = " + member.getUsername());
        }
    }


    @Test
    public void queryHint() {
        Member member1 = memberRepository.save(new Member("member1", 10));
        em.flush();
        em.clear();

//        Member findMember = memberRepository.findById(member1.getId()).get();
        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        // 스냅샷 생성 x 변경감지 기능 무시
        findMember.setUsername("member2");
        //findMember.setUsername("member2");  // member1 -> member2로 이름 바꿈

        em.flush();
    }


    @Test
    public void callCustom() {
        List<Member> result = memberRepository.findMemberCustom();
    }
}