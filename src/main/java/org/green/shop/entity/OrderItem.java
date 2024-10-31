package org.green.shop.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="order_item")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@SequenceGenerator(name="order_item_seq", sequenceName = "order_item_seq", allocationSize = 1)
public class OrderItem extends BaseEntity{
    @Id
    @Column(name="order_item_id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "order_item_seq")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order order;
    private int orderPrice;
    private int count;

    //수량 * 가격 = 가격을 리턴
    public int getTotalPrice(){
        return orderPrice*count;
    }
    //주문취소시 상품의 재고 더하기
    public void cancel(){
        this.getItem().addStock(count);
    }
}
