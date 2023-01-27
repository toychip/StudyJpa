package jpabook.jpashop;

import jpabook.jpashop.domain.*;

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

        try {
//            실제로는 사용 많이 안함 너무 가시성이 안좋기 때문에 query DSL 을 많이 사용함. jpql을 사용할 줄 알면 메뉴얼 보고 따라하기 쉬움
//            CriteriaBuilder cb = em.getCriteriaBuilder();
//            CriteriaQuery<Member> query = cb.createQuery(Member.class);
//
//            Root<Member> m = query.from(Member.class);
//            CriteriaQuery<Member> cq = query.select(m).where(cb.equal(m.get("username"), "kim"));
//            List<Member> resultList = em.createQuery(cq).getResultList();

            Book book = new Book();
            book.setName("JPA");
            book.setAuthor("김영한");

            em.persist(book);

            em.createQuery("select i from Item i where type(i) = Book", Item.class)
                    .getResultList();

//            Member member = new Member();
//            member.setName("member1");
//            em.persist(member);

            //            flush -> commit, query 둘 일때 실행됨
//            db에 query 가 날라가지 않음 data 결과 0,

//            dbConnecttion
//            dbconn.excuteQuery("select * from member");
       //     em.flush(); //위와 같은 이유로 강제로 flush를 해줌
//            JPA를 우회해서 SQL을 실행하기 직전에 영속성 컨텍스트 수동 플러시

//            List<Member> resultList =
//                    em.createNativeQuery("select MEMBER_ID, city, street, zipcode, name from MEMBER",
//                                    Member.class).getResultList();
//
//            for (Member member1 : resultList) {
//                System.out.println("member1 = " + member1);
//
//            }


            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }
}


//      1 / 17 메모
////----------------------------------------------------------------------------------
//    // 시작 부분
//
//    // 저장하는 코드
//    Team team = new Team();
//            team.setName("TeamA");
//                    em.persist(team);
////            team.getMemberTS().add(memberT);
//
////            Member member = new Member
//                    MemberT memberT = new MemberT();
//                    memberT.setUsername("member1");
////            memberT.changeTeam(team);       // 둘 중에 하나만 넣어야함
//                    team.addMemberT(memberT);       // 위와 하나만 골라서 해야함
//                    em.persist(memberT);
//
////            flush, clear을 하지 않으면 1차 캐시 즉 메모리에 남겨져 있게 된다. 그러므로
////            team.getMemberTS().add(memberT);    -> member 의 setter에서 이 코드를 추가하고 연관관계 메소드로 이름 변경하여 편의 사용
////            을 사용해야함 양방향을 할때는 양쪽 둘다 데이터 값을 넣어줘야함
//                    em.flush();
//                    em.clear();
//
//                    MemberT findMember = em.find(MemberT.class, memberT.getId());
////            em.persist(findMember);
////            Team findTeam = findMember.getTeam();
////            System.out.println("--------------------------findTeam = " + findTeam.getName());
//        List<MemberT> memberTS = findMember.getTeam().getMemberTS();
//        for (MemberT m : memberTS) {
//        System.out.println("m.getUsername() = " + m.getUsername());
//        }
