package org.green.shop.service;

import org.green.shop.dto.ItemImgDTO;
import org.green.shop.entity.ItemImg;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ItemImgService {
    //저장
    void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception;
    //itemid에 해당하는 itemimg조회
    List<ItemImg> findByItemid(Long itemId);
    //수정하기
    void updateItemImg(Long itemImgId, MultipartFile multipartFile) throws Exception;
    //엔티티를 dto로 변환
    default ItemImgDTO entityToDto(ItemImg itemImg){
        ItemImgDTO itemImgDTO = ItemImgDTO.builder()
                .id(itemImg.getId())
                .imgName(itemImg.getImgName())
                .imgUrl(itemImg.getImgUrl())
                .oriImgName(itemImg.getOriImgName())
                .repImgYn(itemImg.getRepimgYn())
                .build();
        return itemImgDTO;
    }
}
