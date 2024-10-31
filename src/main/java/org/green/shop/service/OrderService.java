package org.green.shop.service;

import org.green.shop.dto.OrderDTO;
import org.green.shop.dto.OrderHistDTO;

import java.util.List;

public interface OrderService {
    //주문하기
    public Long order(OrderDTO orderDTO,String email);
    //장바구니리스트 주문하기
    public Long orders(List<OrderDTO> orderDTOList,String email);
    //주문내역조회
    public List<OrderHistDTO> getOrderList(String email);
    //주문취소 요청자(현재 로그인한 사용자)와 주문자가 맞는지 확인
    public boolean vaildateOrder(Long orderId, String email);
    //주문취소하기
    public void cancelOrder(Long orderId);
    //주문항목삭제하기
    public void deleteOrder(Long orderId);
}
