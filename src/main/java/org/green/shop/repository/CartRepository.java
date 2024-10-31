package org.green.shop.repository;

import org.green.shop.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Long> {
    //현재로그인한 회원의 cart엔티티 조회
    Cart findByMemberId(Long memberId);
}
