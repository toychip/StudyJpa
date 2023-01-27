package jpql;

import jdk.internal.org.objectweb.asm.tree.IntInsnNode;

import javax.persistence.*;
import java.util.List;

import static jpql.MemberType.ADMIN;

public class JpaMain {

    public static void main(String[] args) {
//            em.persist(team);\
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        //code

        try {
            System.out.println("\n************************ 객체 생성 ************************\n");

//            Team team = new Team();
//            team.setName("teamA");
            Member member = new Member();
            member.setUsername("관리자1");
            em.persist(member);

            Member member2 = new Member();
            member2.setUsername("관리자2");
            em.persist(member2);

//            member.setTeam(team);       // 연관관계 편의 메서드를 지금 만들기.

            System.out.println("\n************************ em.flush ************************\n");
            em.flush();
            System.out.println("\n************************ em.clear ************************");
            em.clear();

            System.out.println("\n************************ JPQL 기본 함수 ************************");

            String concatQuery = "select concat('a', 'b') From Member m ";

            String substringQuery = "select substring(m.username, 2, 3) From Member m ";

            String locateQuery = "select locate('de', 'abcdegf') From Member m ";

            String sizeQuery = "select size(t.members) From Team t ";

            String functionQuery = "select function('group_concat', m.username) From Member m ";


            List<String> result = em.createQuery(concatQuery, String.class)
                    .getResultList();

            List<Integer> result2 = em.createQuery(locateQuery, Integer.class)
                    .getResultList();

            List<Integer> result3 = em.createQuery(sizeQuery, Integer.class)
                    .getResultList();

            List<String> result4 = em.createQuery(functionQuery, String.class)
                    .getResultList();

            for (String s : result) {
                System.out.println("concatQuery = " + s);
            }

            for (Integer integer : result2) {
                System.out.println("locateQuery = " + integer);
            }

            for (Integer integer : result3) {
                System.out.println("sizeQuery = " + integer);
            }

            for (String s : result4) {
                System.out.println("functionQuery = " + s);
            }



//            System.out.println("\n************************ selectTypedQuery ************************");
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

//            System.out.println("\n************************ 페이징 ************************\n");
//            for (int i = 0; i < 100; i++) {
//                Member member = new Member();
//                member.setUsername("member" + i);
//                member.setAge(i);
//                System.out.println("\n************************ em.persist ************************\n");
//                em.persist(member);
//            }
//            List<Member> pagingResult = em.createQuery("select m from Member m order by m.age desc", Member.class)
//                    .setFirstResult(0)
//                    .setMaxResults(10)
//                    .getResultList();
//            System.out.println("pagingResult.size = " + pagingResult.size());
//            for (Member member1 : pagingResult) {
//                System.out.println("member1 = " + member1);
//            }


//            //2023.01.26

//            System.out.println("\n************************ inner join query ************************");
//            String innerJoinQuery = "select m from Member m inner join m.team t";
//            List<Member> result = em.createQuery(innerJoinQuery, Member.class)
//                    .getResultList();
//            System.out.println("result = " + result);
//
//            System.out.println("\n************************ left outer join query ************************");
//            String leftOuterJoinQuery = "select m from Member m left outer join m.team t";  // OUTER 생략 가능
//            List<Member> result2 = em.createQuery(leftOuterJoinQuery, Member.class)
//                    .getResultList();
//            System.out.println("result2 = " + result2);
//
//            System.out.println("\n************************ theta join query ************************");  //cross join
//            String thetaJoinQuery = "select m from Member m,Team t where m.username = t.name";
//            List<Member> result3 = em.createQuery(thetaJoinQuery, Member.class)
//                    .getResultList();
//            System.out.println("result3 = " + result3);
//            System.out.println("result3.size() = " + result3.size());       // 위 조건의 맞는 멤버의 사이즈 즉 몇명인지 구함
//
//            // 회원과 팀을 조인하면서, 팀 이름이 A인 팀만 조인
//            System.out.println("\n************************ 조건 조인 query ************************");
//            String conditionJoinQuery = "select m from Member m left join m.team t on t.name = 'teamA'";
//            List<Member> result4 = em.createQuery(conditionJoinQuery, Member.class)
//                    .getResultList();
//            System.out.println("result4 = " + result4);
//            System.out.println("result4.size() = " + result4.size());
//
//            System.out.println("\n************************ 연관관계가 없는 엔티티 외부조인 query ************************");
//            String irrelevantEntityJoinQuery = "select m from Member m left join Team t on m.username = t.name";
//            // left 를 제외하면 연관 관계가 없는 엔티티 내부조인 가능
//            List<Member> result5 = em.createQuery(irrelevantEntityJoinQuery, Member.class)
//                    .getResultList();
//            System.out.println("result5 = " + result5);
//            System.out.println("result5.size() = " + result5.size());

//              2023.01.27
//            System.out.println("\n************************ JPQL Enum 타입 표현 ************************");
//
//            String query = "select m.username, 'HELLO', true From Member m " +
//                    "where m.age between 0 and 10";
//            //where m.type = :userType";     // Enum 은 패키지부터 경로를 전부 적어줘야 인식가능 jpql.MemberType.ADMIN
//
//            List<Object[]> result = em.createQuery(query)
//                    // .setParameter("userTypee", ADMIN)   // 파라미터 바인딩
//                    .getResultList();
//
//            for (Object[] objects : result) {
//                System.out.println("objects[0] = " + objects[0]);
//                System.out.println("objects[1] = " + objects[1]);
//                System.out.println("objects[2] = " + objects[2]);
//            }
////----------------------------------------조건식----------------------------------------
//            System.out.println("\n************************ 조건식 CASE ************************");
//
//            String query =
//                    "select " +
//                            "case when m.age <= 10 then '학생요금' " +
//                            "     when m.age >= 60 then '경로요금' " +
//                            "     else '일반요금' " +
//                            "end " +
//                            "from Member m";
//            List<String> result = em.createQuery(query, String.class)
//                    .getResultList();
//            for (String s : result) {
//                System.out.println("s = " + s);
//            }
//
//            System.out.println("\n************************ 조건식 COALESCE ************************");
//            // 하나씩 조회해서 null이 아니면 반환
//
//            String coalesceQuery = "select coalesce(m.username, '이름 없는 회원') from Member m ";
//            List<String> result2 = em.createQuery(coalesceQuery, String.class)
//                    .getResultList();
//
//            for (String s2 : result2) {
//                System.out.println("s2 = " + s2);
//            }
//
//            System.out.println("\n************************ 조건식 NULLIF ************************");
//            // 두 값이 같으면 null 반환, 다르면 첫번째 값 반환
//            // 예를 들어 관리자의 이름을 숨겨야할때 사용
//
//            String nullIfQuery = "select nullif(m.username, '관리자') as username " +
//                    "from Member m ";
//            List<String> result3 = em.createQuery(nullIfQuery, String.class)
//                    .getResultList();
//
//            for (String s3 : result3) {
//                System.out.println("s3 = " + s3);
//            }

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
