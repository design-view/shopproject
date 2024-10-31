package org.green.shop.service;

import org.green.shop.dto.MemberDTO;
import org.green.shop.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class MemberServiceTests {
    @Autowired
    MemberService memberService;

    @Test
    public void saveMemberTest(){
        MemberDTO dto = MemberDTO.builder()
        .email("admin@green.com")
                .name("관리자")
                .password("1234")
                .address("울산시 남구 삼산동")
        .build();
        Member member =  memberService.saveMember(dto);
        System.out.println(member.toString());
    }
}
