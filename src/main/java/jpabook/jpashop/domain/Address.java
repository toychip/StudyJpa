package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable     // 어딘가에 내장될수있다.
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;

    protected Address(){        // JPA스펙상 만들어둔것. 함부로 생성 불가
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }       // 변경 불가
}
