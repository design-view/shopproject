package org.green.shop.repository;

import org.green.shop.dto.CartDetailDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CartItemRepositoryTests {
    @Autowired
    CartItemRepository cartItemRepository;

    @Test
    public void findCartDeatilDTOTest(){
        List<CartDetailDTO> result  = cartItemRepository.findCartDetailDTOList(41L);
        for(CartDetailDTO dto: result) {
            System.out.println(dto.toString());
        }
    }
}
