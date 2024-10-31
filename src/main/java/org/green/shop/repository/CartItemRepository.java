package org.green.shop.repository;

import org.green.shop.dto.CartDetailDTO;
import org.green.shop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    //cartItem엔티티조회 카트아이디, 상품아이디
    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    @Query(" select new org.green.shop.dto." +
            "CartDetailDTO(ci.id, i.itemNm, i.price, ci.count, im.imgUrl) " +
            "from CartItem ci " +
            "join ci.item i " +
            "join ItemImg im on im.item.id = i.id " +
            "where ci.cart.id = :cartId " +
            "and im.repimgYn = 'Y' ")
    List<CartDetailDTO> findCartDetailDTOList(@Param("cartId") Long cartId);
}
