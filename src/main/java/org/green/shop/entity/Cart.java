package org.green.shop.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="cart")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@SequenceGenerator(name="cart_seq",sequenceName = "cart_seq",allocationSize = 1)
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "cart_seq")
    @Column(name="cart_id")
    private Long id;
    @OneToOne
    @JoinColumn(name="member_id")
    private Member member;
}
