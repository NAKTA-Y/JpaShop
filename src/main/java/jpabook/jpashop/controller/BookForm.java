package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class BookForm {

    private Long id;

    @NotBlank(message = "상품 이름 입력은 필수 사항입니다.")
    private String name;

    private int price;

    private int stockQuantity;

    private String author;

    private String isbn;
}
