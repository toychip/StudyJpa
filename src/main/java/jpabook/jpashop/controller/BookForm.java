package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class BookForm {
    private Long id;
    // 상품 수정이 있기 떄문에 id가 필수값
    private String name;

    // 공통 속성
    private int price;
    private int stockQuantity;

    private String author;
    private String isbn;
}
