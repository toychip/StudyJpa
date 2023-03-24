package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/*
// Collection이 아닌 xxxToOne (ManyToOne, OneToOne)
1. Order 조회
2. Order -> Member 연관이 걸림
3. Order -> Delivery 연관이 걸림
 */

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    //Entity를 그대로 반환하는 나쁜 예
    //Entity를 그대로 반환하면 어떠한 일이 발생하는지를 보여주는 예
    private final OrderRepository orderRepository;
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findAllByString(new OrderSearch());   // 검색 조건이 없기 때문에 전부를 갖고옴
        for (Order order : all) {
            order.getMember().getName();    //Lazy 강제 초기화
            order.getMember().getAddress();    //Lazy 강제 초기화
        }

        return all;
    }// Order에서 Member로, 또 반대로 무한루프를 돌면서 오류가 생김

    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2(){
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        // 그대로 반환하면 안됨. dto를 거쳐서 반환
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))    // map -> orderDto로 바꿈
                .collect(Collectors.toList());
        return result;
    }
    @Data
    static class SimpleOrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;    // 배송지 정보

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); // LAZY 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); // LAZY 초기화
        }
    }
}
