package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;

@SpringBootTest
public class ItemUpdateTest {

    @Autowired EntityManager em;

//    ItemService is;

    @Test
    public void updateTest() throws Exception {
    //given
    Book book = em.find(Book.class, 1L);

    //TX
    book.setName("asdf");

    //변경감지 == dirty checking
    //TX commit

    }

}
