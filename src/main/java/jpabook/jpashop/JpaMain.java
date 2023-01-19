package jpabook.jpashop;

import jpabook.jpashop.domain.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;


public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {





//            em.persist(book);


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
