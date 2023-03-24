package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

//import javax.persistence.Entity;
import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

//    @PersistenceContext
    private final EntityManager em;

//    @Autowired
//    public MemberRepository(EntityManager em) {
//        this.em = em;
//    }

    public void save(Member member){
        em.persist(member);
    }

    public Member findOne(Long id){
        return em.find(Member.class, id);
    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
//        sql은 테이블을 대상으로 query를 하지만 위에 있는 것은 entity객체를 대상으로 한다.
    }
    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name) //Parameter 바인딩
                .getResultList();
    }
}
