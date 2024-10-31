package org.green.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.green.shop.constant.ItemSellStatus;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {
    private Long id;
    private String itemNm;
    private int price;
    private String itemDetail;
    private int stockNumber;
    private ItemSellStatus itemSellStatus;
    private List<ItemImgDTO> itemImgDTOList = new ArrayList<>();
    private List<Long> itemImgIds = new ArrayList<>();


}
