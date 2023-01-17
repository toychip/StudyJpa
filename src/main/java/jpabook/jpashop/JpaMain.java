package jpabook.jpashop;

import jpabook.jpashop.domain.MemberT;
import jpabook.jpashop.domain.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.lang.reflect.Member;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            // 시작 부분

            // 저장하는 코드
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

//            Member member = new Member
            MemberT memberT = new MemberT();
            memberT.setUsername("member1");

            memberT.setTeam(team);
            em.persist(memberT);

            MemberT findMember = em.find(MemberT.class, memberT.getId());
            Team findTeam = findMember.getTeam();
            System.out.println("--------------------------findTeam = " + findTeam.getName());

            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }
}
