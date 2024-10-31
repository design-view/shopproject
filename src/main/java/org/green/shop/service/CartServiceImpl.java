package org.green.shop.service;

import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.green.shop.dto.CartDetailDTO;
import org.green.shop.dto.CartItemDTO;
import org.green.shop.dto.CartOrderDTO;
import org.green.shop.dto.OrderDTO;
import org.green.shop.entity.Cart;
import org.green.shop.entity.CartItem;
import org.green.shop.entity.Item;
import org.green.shop.entity.Member;
import org.green.shop.repository.CartItemRepository;
import org.green.shop.repository.CartRepository;
import org.green.shop.repository.ItemRepository;
import org.green.shop.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final CartItemRepository cartItemRepository;
    private final ItemRepository itemRepository;
    private final OrderService orderService;


    //cart추가 (insert)
    @Override
    public Long addCart(CartItemDTO cartItemDTO, String email) {
        //현재 로그인한 회원 엔티티를 조회
        Member member = memberRepository.findByEmail(email);
        //장바구니에 담을 상품 조회
        Item item = itemRepository.findById(cartItemDTO.getItemId()).get();
        //아이템의 재고수량을 장바구니 주문수량만큼 감소
        item.removeStock(cartItemDTO.getCount());
        //현재 로그인한 장바구니 엔티티를 조회
        Cart cart = cartRepository.findByMemberId(member.getId());
        //상품을 처음으로 장바구니에 담을경우 해당회원의 장바구니를 생성
        //로그인한 회원의 cart가 없는경우 cart엔티티 생성, insert
        if(cart==null){
            cart = Cart.builder()
                    .member(member)
                    .build();
            cartRepository.save(cart);
        }
        //현재 상품이 장바구니에 있는지 체크
        CartItem cartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(),item.getId());
        //이미담겨있는 경우 (cartItem이 null이 아닐때)
        if(cartItem != null){
            //수량만 전달받은 count더해주기
            cartItem.addCount(cartItemDTO.getCount());
            cartItemRepository.save(cartItem);
            return cartItem.getId();
        }
        //담겨있지 않을때
        else {
            CartItem cartItem1 = CartItem.builder()
                    .cart(cart)
                    .item(item)
                    .count(cartItemDTO.getCount())
                    .build();
            cartItemRepository.save(cartItem1);
            return cartItem1.getId();
        }
    }

    @Override
    public List<CartDetailDTO> getCartList(String email) {
        List<CartDetailDTO> cartDetailDTOList = new ArrayList<>();
        //member객체 조회
        Member member = memberRepository.findByEmail(email);
        //cart객체 조회
        Cart cart = cartRepository.findByMemberId(member.getId());
        //cart가 비었으면 빈 리스트 리턴
        if(cart==null){
            return cartDetailDTOList;
        }
        //조회된 cartDetailDTO리스트 할당
        cartDetailDTOList = cartItemRepository.findCartDetailDTOList(cart.getId());
        return cartDetailDTOList;
    }

    //장바구니 제품 수량변경시 테이블 수량변경
    @Override
    public void updateCartItemCount(Long cartItemId, int count) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).get();
        cartItem.updateCount(count);
        cartItemRepository.save(cartItem);
    }

    @Override
    public void deleteCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).get();
        cartItemRepository.delete(cartItem);
    }

    @Override
    public long orderCartItem(List<CartOrderDTO> cartOrderDTOList, String email) {
        //컨트롤러에서 전달받은 cartOderDTOList --> List<OrderDTO>
        List<OrderDTO> orderDTOList = new ArrayList<>();
        for(CartOrderDTO cartOrderDTO: cartOrderDTOList){
            //CartOrderDTO ---> cartItemId
            //CartOrderDTO로 CartItem엔티티 조회
            CartItem cartItem =
                    cartItemRepository.findById(cartOrderDTO.getCartItemId()).get();
            //OrderDTO ---> count, itemid
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setItemId(cartItem.getItem().getId());
            orderDTO.setCount(cartItem.getCount());
            orderDTOList.add(orderDTO);
        }
        Long orderId = orderService.orders(orderDTOList,email);
        //장바구니에서 장바구니아이템 삭제
        for(CartOrderDTO cartOrderDTO: cartOrderDTOList){
            CartItem cartItem =
                    cartItemRepository.findById(cartOrderDTO.getCartItemId()).get();
            cartItemRepository.delete(cartItem);
        }
        return orderId;
    }


}
