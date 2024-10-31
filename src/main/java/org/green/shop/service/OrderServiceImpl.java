package org.green.shop.service;

import lombok.RequiredArgsConstructor;
import org.green.shop.dto.OrderDTO;
import org.green.shop.dto.OrderHistDTO;
import org.green.shop.dto.OrderItemDTO;
import org.green.shop.entity.*;
import org.green.shop.repository.ItemImgRepository;
import org.green.shop.repository.ItemRepository;
import org.green.shop.repository.MemberRepository;
import org.green.shop.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemImgRepository itemImgRepository;
    @Override
    public Long order(OrderDTO orderDTO, String email) {
        //상품아이디로 상품조회
        Item item = itemRepository.findById(orderDTO.getItemId()).get();
        //email로 member조회
        Member member = memberRepository.findByEmail(email);
        //주문아이템 리스트 생성하기
        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.builder()
                .item(item)
                .count(orderDTO.getCount())
                .orderPrice(item.getPrice())
                .build();
        orderItemList.add(orderItem);
        //아이템의 재고수량이 주문수량만큼 감소
        item.removeStock(orderDTO.getCount());
        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);
        return order.getId();
    }

    //장바구니상품 주문리스트 저장
    @Override
    public Long orders(List<OrderDTO> orderDTOList, String email) {
        Member member = memberRepository.findByEmail(email);
        List<OrderItem> orderItemList = new ArrayList<>();
        for(OrderDTO orderDTO:orderDTOList){
            //주문항목 아이템 조회
            Item item = itemRepository.findById(orderDTO.getItemId()).get();
            OrderItem orderItem = OrderItem.builder()
                    .item(item)
                    .count(orderDTO.getCount())
                    .orderPrice(item.getPrice())
                    .build();
            orderItemList.add(orderItem);

        }
        Order order = Order.createOrder(member,orderItemList);
        orderRepository.save(order);
        return order.getId();
    }

    @Override
    public List<OrderHistDTO> getOrderList(String email) {
        //OrderHistDTO 리스트 리턴
        //조회
        List<Order> orders = orderRepository.findOrders(email);
        //조회한 orders리스트를 OrderHistDTO리스트로 변환하기
        List<OrderHistDTO> histDTOList =
        orders.stream().map(order->entityToDTO(order)).collect(Collectors.toList());
        return histDTOList;
    }

    //취소 요청시 주문자가 맞는지 확인
    @Override
    public boolean vaildateOrder(Long orderId, String email) {
        //요청자 member객체 조회
        Member curMember = memberRepository.findByEmail(email);
        //주문 객체 조회
        Order order = orderRepository.findById(orderId).get();
        Member orderMember = order.getMember();
        //member객체의 email값이 다른지 비교  다르면 false리턴
        if(!StringUtils.equals(curMember.getEmail(),orderMember.getEmail())){
            return false;
        }
        return true;
    }
    //주문 취소 처리
    @Override
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).get();
        order.cancelOrder();
        orderRepository.save(order);
    }

    //삭제하기
    @Override
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).get();
        orderRepository.delete(order);
    }

    //엔티티를 dto로 리턴
    public OrderHistDTO entityToDTO(Order order){
        //OrderHistDTO객체 생성
        OrderHistDTO orderHistDTO = OrderHistDTO.builder()
                .orderId(order.getId())
                .orderDate(order.getOrderDate().toString())
                .orderStatus(order.getOrderStatus())
                .build();
        //orderItems리스트 할당
        List<OrderItem> orderItems = order.getOrderItems();
        List<OrderItemDTO> orderItemDTOs = new ArrayList<>();
        for(OrderItem item : orderItems){
            //대표이미지 엔티티만 조회 (item의 id, "Y")
            //where id=78 and repimgYn='Y'
            ItemImg itemimg = itemImgRepository
                    .findByItemIdAndRepimgYn(item.getItem().getId(),"Y");
            OrderItemDTO orderItemDTO = OrderItemDTO.builder()
                    .itemNm(item.getItem().getItemNm())
                    .count(item.getCount())
                    .imgUrl(itemimg.getImgUrl())
                    .orderPrice(item.getOrderPrice())
                    .build();
            //리스트에 dto객체 추가하기
            orderItemDTOs.add(orderItemDTO);
        }
        //orderHistDTO의 필드 값으로 List<OrderitemDTO>세팅
        orderHistDTO.setOrderItemDTOList(orderItemDTOs);
        return orderHistDTO;

    }

}
