package JPA.SpringDataJpa.repository;


import JPA.SpringDataJpa.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
@Repository     // 복잡한 쿼리는 다음과 같이 분리하기
public class MemberQueryRepository {

    private final EntityManager em;

    List<Member> findAllMembers() {     // 하단 복잡한 Query라고 가정
        return em.createQuery("select m from Member m")
                .getResultList();
    }

}


