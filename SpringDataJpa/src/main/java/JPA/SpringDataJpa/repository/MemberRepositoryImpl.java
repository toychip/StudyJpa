package JPA.SpringDataJpa.repository;

import JPA.SpringDataJpa.entity.Member;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{
// MemberRepositoryCustom -> 이름이 어떤 것이여도 상관 없음
//    MemberRepositoryImpl -> SpringDataJpa가 알아서 구현체로 찾아줌 이름 Impl 붙여야하는 것 꼭 기억하기
    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m")
                .getResultList();
    }
}
