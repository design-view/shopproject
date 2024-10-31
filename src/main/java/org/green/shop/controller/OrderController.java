package org.green.shop.controller;

import lombok.RequiredArgsConstructor;
import org.green.shop.dto.OrderDTO;
import org.green.shop.dto.OrderHistDTO;
import org.green.shop.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/order")
    @CrossOrigin(origins = "http://team4min.jinlabs.net:80")
    //@ResponseBody 자바객체를 http요청의 body로 전달
    //@RequestBody  http요청의 본문 body에 담긴 내용을 자바 객체로 전달
    //@Controller어노테이션이 선언된 클래스에서 메소드 인자로 principal객체를
    //넘겨줄경우 해당 객체에 직접 접근할수 있음 principal객체에는 현재 로그인한 회원의 이메일 정보를 조회
    public @ResponseBody ResponseEntity order(@RequestBody OrderDTO orderDTO, Principal principal){
        System.out.println("오더 호출");
        String email = principal.getName();
        Long orderId;
        try{
            orderId = orderService.order(orderDTO,email);
        }catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }
    //주문목록조회
    @GetMapping("/orders")
    @CrossOrigin(origins = "http://team4min.jinlabs.net:80")
    public String orderHist(Principal principal, Model model){
        List<OrderHistDTO> result = orderService
                .getOrderList(principal.getName());
        model.addAttribute("result",result);
        return "order/orderHist";
    }
    //주문취소하기
    @PostMapping("/order/{orderId}/cancel")
    @CrossOrigin(origins = "http://team4min.jinlabs.net:80")
    public @ResponseBody ResponseEntity cancelOrder(@PathVariable("orderId") Long orderId,
                                                    Principal principal){
        //로그인한 사용자와 주문한 사용자가 일치하는지 체크
        //일치하지않은 경우 권한없음을 응답해줌
        if(!orderService.vaildateOrder(orderId,principal.getName())){
            return new ResponseEntity<String>("주문취소 권한이 없습니다."
                    ,HttpStatus.FORBIDDEN);
        }
        orderService.cancelOrder(orderId);
        return new ResponseEntity<Long>(orderId,HttpStatus.OK);
    }
    //주문이력삭제하기
    @DeleteMapping("/order/{orderId}")
    @CrossOrigin(origins = "http://team4min.jinlabs.net:80")
    public @ResponseBody ResponseEntity deleteOrder(@PathVariable("orderId") Long orderId){
        orderService.deleteOrder(orderId);
        return new ResponseEntity<Long>(orderId,HttpStatus.OK);
    }
}
