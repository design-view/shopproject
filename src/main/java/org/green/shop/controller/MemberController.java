package org.green.shop.controller;

import lombok.RequiredArgsConstructor;
import org.green.shop.dto.MemberDTO;
import org.green.shop.entity.Member;
import org.green.shop.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/join_page")
    public String memberForm(){

        return "member/memberForm";
    }
    @PostMapping("/join")
    public String memberForm(MemberDTO memberDTO){
        Member member = memberService.saveMember(memberDTO);
        System.out.println("가입:"+member.toString());
        return "redirect:/";
    }
    @PostMapping("/emailcheck")
    public ResponseEntity<String> memberMailCheck(MemberDTO memberDTO){
        String result = memberService.validateMember(memberDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @GetMapping("/login_page")
    public String loginMember(){
        return "member/memberLogin";
    }
    @GetMapping("/login/error")
    public String loginMember(Model model){
        model.addAttribute("loginErrorMsg",
                "아이디 또는 비밀번호를 확인해주세요");
        return "member/memberLogin";
    }
}
