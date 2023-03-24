package jpabook.jpashop.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Member extends BaseEntity{

    @Id         // Entity에선 식별자를 필수로 해줘야ㅕ함.
    @GeneratedValue     // 생략하면 strategy가 auto
    @Column(name = "MEMBER_ID")
    private Long id;
    private String name;

    @Embedded
    private Address address;
//    private String city;
//    private String street;
//    private String zipcode;


    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
