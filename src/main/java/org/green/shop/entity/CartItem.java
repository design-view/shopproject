package org.green.shop.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="cart_item")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@SequenceGenerator(name="cartItem_seq",sequenceName = "cartItem_seq", allocationSize = 1)
public class CartItem  {
    @Id
    @Column(name="cart_item_id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "cartItem_seq")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cart_id")
    private Cart cart;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;
    private int count;

    //담겨있는 상품일때 기존수량에 현재 담을 수량을 더하기
    public void addCount(int count){
        this.count += count;
    }
    //수량이 변경될때 수량변경
    public void updateCount(int count){
        this.count = count;
    }
}
