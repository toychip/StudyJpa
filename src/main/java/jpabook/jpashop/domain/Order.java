package jpabook.jpashop.domain;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")         // FK 이름
    private Member member;

    @OneToMany(mappedBy = "order" ,cascade = CascadeType.ALL)      // order에 의해 매핑이 된다는 것이다.

//    em.persist(orderItemA)
//    em.persist(orderItemB)
//    em.persist(orderItemC)
//    em.persist(order)
//    원래 이렇게 저장해야하는 것을 CascadeType.ALL을 사용하믕로써 db에 한번의 명령으로 보낸다.
//    위 코드가     em.persist(order) 한번으로 해결 가능


    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;
//    order 를 Persit 하면 Delivery도 한 번에 Persit가된다.

    private LocalDateTime orderDate; // 주문 시간

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // 주문 상태 [ORDER, CANCEL]

    // ==== 연관관계 편의 메서드 ==== //

    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelvery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    // =========생성 메서드========= //
    public static Order cretateOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem: orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }
    // 비즈니스 로직//
    /*
    주문 취소
     */
    public void cancel(){
        if (delivery.getDeliveryStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송 완료된 상품은 취소가 불가능합니다.");
        }
        this.setOrderStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem: orderItems) {
            orderItem.cancel();
        }
    }
    // 조회 로직 //
    /*
    전체 주문 가격 조회
     */
    public int getTotalPrice(){
//        int totalPrice = 0 ;
//        for (OrderItem orderItem : orderItems){
//            totalPrice += orderItem.getTotalPrice();
//        }
//        return totalPrice;
        return orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
    }
}
