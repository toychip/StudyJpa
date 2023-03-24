package hellojpa;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

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

            member.getFavoriteFoods().add("치킨");
            member.getFavoriteFoods().add("족발");
            member.getFavoriteFoods().add("피자");

            member.getAddressHistory().add(new AddressEntity("Old1", "수지구", "10001"));
            member.getAddressHistory().add(new AddressEntity("Old2", "하동구", "9999"));

            em.persist(member);

            em.flush();
            em.clear();

            System.out.println("====================START====================");
            // wㅣ연로딩임
            Member findMember = em.find(Member.class, member.getId());

//            List<Address> addressHistory = findMember.getAddressHistory();
//            for (Address address : addressHistory) {
//                System.out.println("address = " + address.getCity());
//            }
//
//            Set<String> favoriteFoods = findMember.getFavoriteFoods();
//            for (String favoriteFood : favoriteFoods) {
//                System.out.println("favoriteFood = " + favoriteFood);
//            }

            // homeCity -> newCity 로 이사감
            // findMember.getHomeAddress().setCity("newCity"); // -> 이렇게하면 안됨. 참조값을 넣는 것이므로 모두가 다 바뀜
            Address memberNewAdress = findMember.getHomeAddress();
            String findZipCode = findMember.getHomeAddress().getZipCode();
            String findStreet = findMember.getHomeAddress().getStreet();
            // findStreet or memberNewAdress.getStreet() 위으 ㅣ여러가지 방법 중 원하는 것으로 하기
            findMember.setHomeAddress(new Address("newCity", findStreet, memberNewAdress.getZipCode()));

            // 치킨을 한식으로 바꿈
            findMember.getFavoriteFoods().remove("치킨");
            findMember.getFavoriteFoods().add("한식");

            // 갓 하나만 바꾸기
//                                                                                                                                                  System.out.println("====================Address====================");
            findMember.getAddressHistory().remove(new AddressEntity("Old1", "수지구", "10001"));
            findMember.getAddressHistory().add(new AddressEntity("newCity1", "수지구", "10001"));

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
            em.createNativeQuery("select MEMBER_ID, city, street, zipcode, USERNAME from MEMBER").getResultList();


            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }
}
