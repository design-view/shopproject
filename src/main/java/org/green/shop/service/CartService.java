package org.green.shop.service;

import org.green.shop.dto.CartDetailDTO;
import org.green.shop.dto.CartItemDTO;
import org.green.shop.dto.CartOrderDTO;

import java.util.List;

public interface CartService {
    //카트 추가하기 (카드엔티티는 member참조하므로 member객체 생성을
    //위해 email전달받기)
    public Long addCart(CartItemDTO cartItemDTO, String email);
    //카트아이템조회해서 catrDTO리스트로 리턴
    public List<CartDetailDTO> getCartList(String email);
    //카트아이템 수량 변경
    public void updateCartItemCount(Long cartItemId, int count);
    //카트아이템 삭제
    public void deleteCartItem(Long cartItemId);
    //카트목록 주문으로 저장
    public long orderCartItem(List<CartOrderDTO> cartOrderDTOList,String email);
}
