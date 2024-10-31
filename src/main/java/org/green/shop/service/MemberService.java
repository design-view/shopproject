package org.green.shop.service;

import org.green.shop.constant.Role;
import org.green.shop.dto.MemberDTO;
import org.green.shop.entity.Member;

public interface MemberService {
    //회원가입
    Member saveMember(MemberDTO dto);
    //이메일 중복체크
    String validateMember(MemberDTO dto);

    default Member dtoToEntity(MemberDTO dto){
        Member member = Member.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .address(dto.getAddress())
                .role(Role.USER)
                .build();
        return member;
    }
}
