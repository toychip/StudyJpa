package JPA.SpringDataJpa.repository;

import JPA.SpringDataJpa.dto.MemberDto;
import JPA.SpringDataJpa.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom{

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

    // 파라미터 바인딩
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    List<Member> findListByUsername(String username);    // 컬렉션

    Member findMemberByUsername(String username); // 단건

    Optional<Member> findOptionalByUsername(String username);   // 단건 Optional

//    @Query(value = "select m from Member m", countQuery = "select count(m.username) from Member m")
//    카운트를 하는데 외부조인을 할 필요가 전혀 없음 하지만 jpa는 조인을 하므로 countQuery따로 생성해주기

    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);
//    Slice<Member> findByAge(int age, Pageable pageable);
//                                  Pageable : query에 대한 조건

    @Modifying(clearAutomatically = true) // 있어야 excuteUpdate가 실행됨, bulk연산시 필수
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);


    @Query("select m from Member m left join fetch m.team")
        // fetch를 사용하여 member를 select할때 team도 한 번에 같이 조회함
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    // Query + Entity 직접 짠 쿼리문에 fetchjoin 하며 EntityGraph로 사용하는 법
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    // 단순히 읽기만 하기 위해, 메모리를 2번 먹는 것을 방지하기 위해 한 번만 사용하는 방법.
    @QueryHints(
            @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    @Query(value = "select * from member where username = ?", nativeQuery = true)
    Member findByNativeQuery(String username);

    @Query(value = "select m.member_id as id, m.username, t.name as teamName " +
            "from member m left join team t",
            countQuery = "select count(*) from member",
            nativeQuery = true)
    Page<MemberProjection> findByNagiveProjection(Pageable pageable);
}
