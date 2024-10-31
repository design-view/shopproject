package org.green.shop.service;

import org.green.shop.dto.ItemDTO;
import org.green.shop.dto.MainItemDTO;
import org.green.shop.dto.PageRequestDTO;
import org.green.shop.dto.PageResultDTO;
import org.green.shop.entity.Item;
import org.green.shop.entity.ItemImg;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ItemService {
    //아이템 저장(insert)
    Long saveItem(ItemDTO itemDTO, List<MultipartFile> itemimgFileList) throws Exception;
    //아이템 조회(페이징조회)
    PageResultDTO<ItemDTO,Item> getList(PageRequestDTO requestDTO);
    //아이템 하나조회
    ItemDTO getItem(Long itemid);
    //아이템수정하기
    Long updateItem(ItemDTO itemDTO, List<MultipartFile> itemImgFileList) throws Exception;
    //메인상품조회
    PageResultDTO<MainItemDTO, Object[]> getMainList(PageRequestDTO requestDTO);
    //상품리스트조회
    PageResultDTO<MainItemDTO, Object[]> getShopList(PageRequestDTO requestDTO);
    default Item dtoToEntity(ItemDTO dto){
        Item item = Item.builder()
                .itemNm(dto.getItemNm())
                .itemDetail(dto.getItemDetail())
                .itemSellStatus(dto.getItemSellStatus())
                .stockNumber(dto.getStockNumber())
                .price(dto.getPrice())
                .build();
        return item;
    }
    //엔티티를 dto로 변환
    default ItemDTO entityToDto(Item item){
        ItemDTO itemDTO = ItemDTO.builder()
                .id(item.getId())
                .itemNm(item.getItemNm())
                .itemDetail(item.getItemDetail())
                .price(item.getPrice())
                .stockNumber(item.getStockNumber())
                .itemSellStatus(item.getItemSellStatus())
                .build();
        return itemDTO;
    }
    //오브젝트배열 MainItemDTO로 변환
    default MainItemDTO entityObjToDTO(Item item, ItemImg itemimg){
        MainItemDTO mainDTO = MainItemDTO.builder()
                .id(item.getId())
                .itemNm(item.getItemNm())
                .stockNumber(item.getStockNumber())
                .price(item.getPrice())
                .itemSellStatus(item.getItemSellStatus())
                .imgUrl(itemimg.getImgUrl())
                .build();
        return mainDTO;
    }
}
