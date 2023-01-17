package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        //code

        try {
            // 비영속 상태
//            Member member = em.find(Member.class, 1L);
            Member member = new Member();
//            member.setId();
            member.setAge(12);
//            member.setDescription("asd");
//            member.setCreatedDate(21010101);
//            member.setRoleType(123);
            member.setName("JpaChangeName");
            // 업데이트시 em.persist를 사용하지 않아도된다.
            // 영속 상태
            em.persist(member);
            // 이때 쿼리가 날라가는 것이 아닌, 밑에 tx.commit할때 날라간다.

            // 영속성 컨텐츠에서 배제하기, 더이상 관리 안함.
            // em.detach(member);

            // 영속성 전부 지우기
            // em.clear();

//            List<Member> result =  em.createQuery("select m from Member", Member.class)
//                    .setFirstResult(1)
//                    .setMaxResults(10)
//                    // 1~10 까지의 데이터를 갖고 와라
//                    .getResultList();
//            for (Member member : result) {
//                System.out.println("member.name = " + member.getName());
//            }
            System.out.println("=========");
            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }
}
