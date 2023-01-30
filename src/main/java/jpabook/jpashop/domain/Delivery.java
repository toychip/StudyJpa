package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    private String name;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    // PhysicalNamingStrategy에 들어가면 테이블, 컬럼명을 생성 및 수정할 수 있다.

    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;  // READY, COMP
}
