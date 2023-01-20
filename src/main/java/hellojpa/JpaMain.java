package hellojpa;

import javax.persistence.*;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        //code

        try {

            Member member = new Member();
            member.setUsername("준형");
            member.setHomeAddress(new Address("수원시", "영통구", "11111"));
            member.setWorkPeriod(new Period());

            em.persist(member);
//            -----------------------------------------------------------------------------------
//            Child child1 = new Child();
//            Child child2 = new Child();
//
//            Parent parent = new Parent();
//            parent.addChild(child1);
//            parent.addChild(child2);
//            em.persist(parent);
//
////            cascade = CascadeType.ALL 를 사용하여 아래 생략 가능
//            em.persist(child1);
//            em.persist(child2);
//
//            em.flush();
//            em.clear();
//
//            Parent findParent = em.find(Parent.class, parent.getId());
//-------------------------------------------------------------------------------------------------
//            findParent.getChildList().remove(0);
//             orphanRemoval = true 조건을 추가했기 때문에 remove 를 해서 childList 에서 빠진 고아 객체들은 삭제가 된다.
//             이것도 cascade = CascadeType.ALL 과 마찬가지로 참조하는 곳이 하나일때만 사용해야한다.

//            em.remove(findParent);
//            findParent.getChildList().remove(0);


            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }
}
