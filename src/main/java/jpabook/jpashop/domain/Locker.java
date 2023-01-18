package jpabook.jpashop.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Locker {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    // 양방향, 즉 단방향 2개로 Locker에서도 조회하고 싶다면

    @OneToOne(mappedBy = "locker")      // 읽기 전용
    private MemberT memberT;
}
