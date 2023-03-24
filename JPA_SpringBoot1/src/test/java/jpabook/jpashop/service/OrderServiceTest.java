package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
public class OrderServiceTest {
    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
    //given
    Member member = createMember();
    Item item = createBook( "시골 JPA", 10000, 10);
    int orderCount = 10;

        //when
    Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

    //then
    Order getOrder = orderRepository.findOne(orderId);
//    assertEquals(OrderStatus.ORDER, getOrder.getOrderStatus(), "상품 주문시 상태는 ORDER");
    assertEquals(1, getOrder.getOrderItems().size(), "주문한 상품 종류 수가 정확해야 한다.");
    assertEquals(10000 * orderCount, getOrder.getTotalPrice(), "주문 가격은 가격 * 수량이다. ");
//    assertEquals(8, item.getStockQuantity(), "주문 수량만큼 재고가 줄어야한다. ");

//    Assertions.assertThrows("주문한 상품 종류 수가 정확해야 한다.", 1, getOrder.getOrderItems().size());
//    assertEquals("상품 주문시 상태는 Order", OrderStatus.ORDER, );
    }

    @Test //(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception {
    //given
        assertThrows(NotEnoughStockException.class, () -> {
            // given
        Member member = createMember();
        Item item = createBook( "시골 JPA", 10000, 10);
        int orderCount = 11;

        //when
        orderService.order(member.getId(), item.getId(), orderCount); // 여기서 exception이 터져야함
        //then
        fail("재고 수량 예외가 발생해야합니다. ");
        });
    }
    @Test
    public void 주문취소() throws Exception {
    //given
    Member member = createMember();
    Item item = createBook( "시골 JPA", 10000, 10);
    int ordercount = 10;
    Long orderid = orderService.order(member.getId(), item.getId(), ordercount);
    //when
    orderService.cancelOrder(orderid);  //  주문을 취소했을 때
    //then
    Order getOrder = orderRepository.findOne(orderid);
//    assertEquals(OrderStatus.CANCEL, getOrder.getOrderStatus(), "주문 취소시 상태는 CanCel이다.");
    assertEquals(10, item.getStockQuantity(), "주문이 취소된 상품은 그만큼 재고가 증가해야 한다. ");
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "성북구", "123-123"));
        em.persist(member);
        return member;
    }
}