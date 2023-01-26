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
            System.out.println("\n************************ 객체 생성 ************************\n");

            for (int i = 0; i < 100; i++) {
                Member member = new Member();
                member.setUsername("member" + i);
                member.setAge(i);
//                System.out.println("\n************************ em.persist ************************\n");
                em.persist(member);
            }

            System.out.println("\n************************ em.flush ************************\n");
            em.flush();

            System.out.println("\n************************ em.clear ************************");
            em.clear();
            System.out.println("\n************************ em.clear 종료 ************************");

            System.out.println("\n************************ 페이징 ************************\n");
            List<Member> pagingResult = em.createQuery("select m from Member m order by m.age desc", Member.class)
                    .setFirstResult(0)
                    .setMaxResults(10)
                    .getResultList();
            System.out.println("pagingResult.size = " + pagingResult.size());
            for (Member member1 : pagingResult) {
                System.out.println("member1 = " + member1);
            }


//            TypedQuery<Member> query = em.createQuery("select m from Member m where m.username = :username", Member.class);
//            query.setParameter("username", "member1");
//            Member singleResult = query.getSingleResult();
//            System.out.println("singleResult = " + singleResult.getUsername());
            //      singleResult = member1
//            // 위를 아래로 줄여씀
//            Member result = em.createQuery("select m from Member m where m.username = :username", Member.class)
//                    .setParameter("username", "member1")
//                    .getSingleResult();
//            System.out.println("singleResult = " + result.getUsername());


//            TypedQuery<Member> query = em.createQuery("select m From Member m ", Member.class);
//            컬렉션 반환
//            결과가 없으면 빈 리스트 반환
//            List<Member> resultList2 = query.getResultList();
//            for (Member member1 : resultList2) {
//                System.out.println("member1 = " + member1);
//            }


//            TypedQuery<Member> query4 = em.createQuery("select m From Member m where m.age = '10' ", Member.class);
//            결과가 정확히 하나, 단일 객체 반환
//            Member result = query4.getSingleResult();
//            SingleResult .. 안나오거나 많이 나왔을 경우 아래와 같다.
//            결과가 없으면 빈 리스트 반환 javax.persistence.NoResultException
//            결과가 둘 이상이면 javax.persistence.NoUniqueResultException
//            Spring Data Jpa -> 결과가 없으면 NULL 반환
//            System.out.println("result = " + result);


//            반환 타입이 문자열일때
//            TypedQuery<String> query2 = em.createQuery("select m.username From Member m ", String.class);

//            TypedQuery<String> query2 = em.createQuery("select m.username m.age From Member m ", String.class);
//             반환 타입 2가지, 이럴때 사용하는 것이 Query
//             타입이 명확하지 않을때
//            Query query3 = em.createQuery("select m.username m.age From Member m ");


//            // --------------------------------------------여러 값 조회--------------------------------------------
//            // Query 타입으로 조회
//            System.out.println("\n************************ Query 타입으로 조회 ************************\n");
//            List manyValue = em.createQuery("select m.username, m.age from Member m").getResultList();
//
//            Object o = manyValue.get(0);
//            Object[] insideValue = (Object[]) o;
//            System.out.println("username = " + insideValue[0]);
//            System.out.println("age = " + insideValue[1]);
//
//            // TypeQuery 방식, Object[]타입으로 조회
//            System.out.println("\n************************ TypeQuery 방식, Object[]타입으로 조회 ************************\n");
//            List<Object[]> TQWay = em.createQuery("select m.username, m.age from Member m").getResultList();
//
//            Object[] TQWayList = TQWay.get(0);
//            System.out.println("username = " + TQWayList[0]);
//            System.out.println("age = " + TQWayList[1]);
//
////            ***************************new 명령어로 조회*************************** 제일 많이 사용되는 좋은 방법
//            // 단순 값을 DTO로 바로 조회
//            // ex) SELECT new jpabook.jpqlUserDTO(m.username, m.age) FROM Member m
//            // 패키지 명을 포함한 전체 클래스 명 입력
//            // 순서와 타입이 일치하는 생성자가 필요하다.
//
//            System.out.println("\n************************ new 명령어로 조회 ************************\n");
//            List<MemberDTO> newWay = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m",
//                    MemberDTO.class).getResultList();
//            MemberDTO memberDTO = newWay.get(0);
//            System.out.println("username = " + memberDTO.getUsername());
//            System.out.println("age = " + memberDTO.getAge());
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
