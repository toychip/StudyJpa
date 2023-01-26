package jpql;

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
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            TypedQuery<Member> query = em.createQuery("select m From member m ", Member.class);
//            컬렉션 반환
            List<Member> resultList2 = query.getResultList();
            for (Member member1 : resultList2) {
                System.out.println("member1 = " + member1);
            }


            TypedQuery<Member> query4 = em.createQuery("select m From member m where m.id = '3' ", Member.class);
//            값이 하나인 경우에
            Member result = query4.getSingleResult();
//            SingleResult 인데 안나오거나 많이 나왔을 경우에 어떻게 할것이냐
//            결과가 없으면 빈 리스트 반환 javax.persistence.NoResultException
//            결과가 둘 이상이면 javax.persistence.NoUniqueResultException
            System.out.println("result = " + result);


//            반환 타입이 문자열일때
            TypedQuery<String> query2 = em.createQuery("select m.username From member m ", String.class);

//            TypedQuery<String> query2 = em.createQuery("select m.username m.age From member m ", String.class);
//             반환 타입 2가지, 이럴때 사용하는 것이 Query
//             타입이 명확하지 않을때
            Query query3 = em.createQuery("select m.username m.age From member m ");


            tx.commit();
        }catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        }finally {
            em.close();
        }
        emf.close();
    }
}
