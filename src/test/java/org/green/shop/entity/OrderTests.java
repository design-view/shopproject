package org.green.shop.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.green.shop.constant.ItemSellStatus;
import org.green.shop.repository.ItemRepository;
import org.green.shop.repository.OrderItemRepository;
import org.green.shop.repository.OrderRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
@Transactional
public class OrderTests {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    OrderItemRepository orderItemRepository;

    @Test
    public void casecadeTest(){
        Order order = new Order();
        for (int i = 0; i < 3; i++) {
            Item item = Item.builder()
                    .price(10000)
                    .itemNm("테스트테스트상품")
                    .itemDetail("상세설명"+i)
                    .stockNumber(10)
                    .itemSellStatus(ItemSellStatus.SELL)
                    .build();
            itemRepository.save(item);
            OrderItem orderItem = OrderItem.builder()
                    .item(item)
                    .count(10)
                    .order(order)
                    .orderPrice(100000)
                    .build();
            order.getOrderItems().add(orderItem);
        }
        orderRepository.save(order);
    }
    public Order createOrder(){
        Order order = new Order();
        for (int i = 0; i < 3; i++) {
            Item item = Item.builder()
                    .price(10000)
                    .itemNm("테스트테스트상품")
                    .itemDetail("상세설명"+i)
                    .stockNumber(10)
                    .itemSellStatus(ItemSellStatus.SELL)
                    .build();
            itemRepository.save(item);
            OrderItem orderItem = OrderItem.builder()
                    .item(item)
                    .count(10)
                    .order(order)
                    .orderPrice(100000)
                    .build();
            order.getOrderItems().add(orderItem);
        }
        orderRepository.save(order);
        return order;
    }
    @Test
    @DisplayName("고아객체삭제하기")
    public void orPahnRemoveTest(){
        Order order = this.createOrder();
        order.getOrderItems().remove(0);

        Long orderItemId = order.getOrderItems().get(0).getId();
        Optional<OrderItem> result = orderItemRepository.findById(orderItemId);
        System.out.println("클래스명 : " + result.get().getOrder().getClass());
    }
}
