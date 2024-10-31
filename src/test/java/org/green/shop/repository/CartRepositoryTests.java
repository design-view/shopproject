package org.green.shop.repository;

import org.green.shop.entity.Cart;
import org.green.shop.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class CartRepositoryTests {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Test
    @DisplayName("장바구니 회원 엔티티 매핑조회")
    public void findCartAndMemberTest(){
        Member member = Member.builder()
                .address("울산시 남구")
                .name("수요일")
                .email("su@naver.com")
                .password(passwordEncoder.encode("1234"))
                .build();
        memberRepository.save(member);
        Cart cart = Cart.builder()
                .member(member)
                .build();
        cartRepository.save(cart);

    }
}
