package org.green.shop.entity;

import jakarta.persistence.*;
import lombok.*;
import org.green.shop.constant.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@SequenceGenerator(name="order_seq", sequenceName = "order_seq", allocationSize = 1)
public class Order extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "order_seq")
    @Column(name="order_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;                 //회원참조
    private LocalDateTime orderDate;       //주문일
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;       //주문상태
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL
            ,orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    //orderItems에는 주문상품정보를 담아줍니다.
    //orderItem객체를 order객체의 orderItems에 담아줍니다.
    //Order엔티티와 OrderItem엔티티가 양방향 참조관계이므로 orderItem객체에도
    //order객체를 세팅해줍니다.
    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
    //order객체 리턴
    public static Order createOrder(Member member, List<OrderItem> orderItems){
        Order order = new Order();
        order.setMember(member);
        for(OrderItem orderItem: orderItems){
            order.addOrderItem(orderItem);
        }
        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }
    //전체금액 리턴
    //orderItems리스트를 돌면서 orderItem.getTotalPrice()를 합계를 구해서 리턴
    public int getTotalPrice(){
        int totalPrice = 0;
        for(OrderItem orderItem: orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
    //주문취소하기
    public void cancelOrder(){
        this.orderStatus = OrderStatus.CANCEL;
        for(OrderItem orderItem: orderItems){
            orderItem.cancel();
        }
    }
}
