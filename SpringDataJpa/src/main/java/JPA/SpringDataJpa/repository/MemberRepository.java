package JPA.SpringDataJpa.repository;

import JPA.SpringDataJpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findTop3HelloBy();

    /* Spring Data JPA 기본 메서드
    COUNT: count...By 반환타입 long
    EXISTS: exists...By 반환타입 boolean
    삭제: delete...By, remove...By 반환타입 long
    DISTINCT: findDistinct, findMemberDistinctBy
    LIMIT: findFirst3, findFirst, findTop, findTop3
     */
}
