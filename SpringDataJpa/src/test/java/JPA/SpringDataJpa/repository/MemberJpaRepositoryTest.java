package JPA.SpringDataJpa.repository;

import JPA.SpringDataJpa.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
// SpringBootTest + Transactional 이 있으면 자동으로 롤백을 시킴
@Rollback(value = false)
class MemberJpaRepositoryTest {

    @Autowired MemberJpaRepository memberJpaRepository;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.find(savedMember.getId());
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("memberA");
        Member member2 = new Member("memberB");

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        // 단건 조회 검증
        Member findmember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findmember2 = memberJpaRepository.findById(member2.getId()).get();
        assertThat(findmember1).isEqualTo(findmember1);
        assertThat(findmember2).isEqualTo(findmember2);

        // 리스트 조회 검증
        List<Member> all = memberJpaRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 카운트 검증
        long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);

        long deletedCount = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);
    }

    @Test
    void findByUsernameAndAgegreaterThen() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);

        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);

        List<Member> reuslt = memberJpaRepository.findByUsernameAndAgeGreaterThen("AAA", 15);

        assertThat(reuslt.get(0).getUsername()).isEqualTo("AAA");
        assertThat(reuslt.get(0).getAge()).isEqualTo(20);
        assertThat(reuslt.size()).isEqualTo(1);
    }


}