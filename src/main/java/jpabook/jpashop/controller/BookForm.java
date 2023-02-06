package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class BookForm {
    // 상품 수정이 있기 떄문에 id가 필수값
    private Long id;
    private String name;
    private int price; // 공통 속성
    private int stockQuantity;
    private String author;
    private String isbn;
}
