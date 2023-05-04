package JPA.SpringDataJpa.repository;

import JPA.SpringDataJpa.dto.MemberDto;
import JPA.SpringDataJpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 쿼리가 단순할 경우
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findTop3HelloBy();

    /* Spring Data JPA 기본 메서드
    COUNT: count...By 반환타입 long
    EXISTS: exists...By 반환타입 boolean
    삭제: delete...By, remove...By 반환타입 long
    DISTINCT: findDistinct, findMemberDistinctBy
    LIMIT: findFirst3, findFirst, findTop, findTop3
     */

    // 쿼리가 복잡할 경우
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    // @Query, DTO 조회
    @Query("select m.username from Member m")
    List<String> findUsernameList();

    // DTo로 할땐 new Opseration 꼭 해야함
    @Query("select new JPA.SpringDataJpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();
}
