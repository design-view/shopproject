package org.green.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.security.Principal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CartDetailDTO {
    private Long cartItemId;   //장바구니상품 아이디
    private String itemNm;     //상품이름
    private int price;         //상품가격
    private int count;         //수량
    private String imgUrl;     //상품 이미지 경로
}
