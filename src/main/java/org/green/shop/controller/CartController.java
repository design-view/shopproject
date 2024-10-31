package org.green.shop.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.green.shop.dto.CartDetailDTO;
import org.green.shop.dto.CartItemDTO;
import org.green.shop.dto.CartOrderDTO;
import org.green.shop.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/cart")
    public @ResponseBody ResponseEntity addCart(@RequestBody CartItemDTO cartItemDTO
            , Principal principal){
        String email =  principal.getName();
        Long cartItemId;
        try {
            cartItemId = cartService.addCart(cartItemDTO,email);
        }catch(Exception error){
            return new ResponseEntity<String>(error.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }
    @GetMapping("/cart")
    public String cartList(Principal principal, Model model){
       List<CartDetailDTO> result = cartService.getCartList(principal.getName());
       model.addAttribute("result",result);
       return "cart/cartList";
    }

    //장바구니아이템 수량 수정
    @PostMapping("/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity upldateCartItem(
            @PathVariable("cartItemId") Long cartItemId, @RequestParam("count") int count){
        if(count <= 0){
            return new ResponseEntity<String>("최소 1개 이상 담아주세요"
                    ,HttpStatus.BAD_REQUEST);
        }
        cartService.updateCartItemCount(cartItemId,count);
        return new ResponseEntity<Long>(cartItemId,HttpStatus.OK);
    }
    //장바구니아이템삭제
    @DeleteMapping("/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity deleteCartItem(
            @PathVariable("cartItemId") Long cartItemId){
        cartService.deleteCartItem(cartItemId);
        return new ResponseEntity<Long>(cartItemId,HttpStatus.OK);
    }
    //장바구니아이템 주문하고 장바구니 아이템 삭제
    @PostMapping("/cart/order")
    //{ cartOrderDtoList : dataList }
    public @ResponseBody ResponseEntity orderCartItem(@RequestBody CartOrderDTO cartOrderDTO
            ,Principal principal){
        List<CartOrderDTO> cartOrderDTOList =  cartOrderDTO.getCartOrderDtoList();
        for(CartOrderDTO cartOrderDTO1: cartOrderDTOList){
            System.out.println(cartOrderDTO1.toString());
        }
        if(cartOrderDTOList==null || cartOrderDTOList.size() == 0 ){
            return new ResponseEntity<String>("주문할 상품을 선택하세요",HttpStatus.BAD_REQUEST);
        }
        Long orderId = cartService.orderCartItem(cartOrderDTOList, principal.getName());
        return new ResponseEntity<Long>(orderId,HttpStatus.OK);
    }
}
