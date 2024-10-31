package org.green.shop.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.green.shop.dto.MemberDTO;
import org.green.shop.entity.Member;
import org.green.shop.repository.MemberRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService, UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member saveMember(MemberDTO dto) {
        //인터페이스에 선언된 dtoToEntity를 호출해서 member를 반환
        Member member = dtoToEntity(dto);
        //member객체의 password필드값은 암호화한 값으로 변경
        member.setPassword(passwordEncoder.encode(dto.getPassword()));
        //레포짓토리.save() --->  테이블에 레코드 저장(insert문 실행)
        return memberRepository.save(member);
    }

    @Override
    public String validateMember(MemberDTO dto) {
        //이메일로 찾은 멤버가 있으면 "ok"를 리턴 , 없으면 "fail"을 리턴
        //"ok" ---> 이미 회원가입되어있는 email주소 입니다.
        //"fail" ---> 등록가능한 email주소 입니다.
        Member findMember =  memberRepository.findByEmail(dto.getEmail());
        if(findMember != null) {
            return "ok";
        }
        return "fail";
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username);
        if(member == null){
            //예외를 발생
            throw new UsernameNotFoundException(username);
        }
        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
}
