package org.green.shop.repository;

import org.green.shop.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository
        extends JpaRepository<ItemImg,Long> {

    List<ItemImg> findByItemId(Long itemId);
    ItemImg findByItemIdAndRepimgYn(Long itemId, String repimgYn);
}
