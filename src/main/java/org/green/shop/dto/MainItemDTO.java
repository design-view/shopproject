package org.green.shop.dto;

import lombok.*;
import org.green.shop.constant.ItemSellStatus;
import org.green.shop.service.ItemService;

@Builder
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MainItemDTO {
    private Long id;
    private String itemNm;
    private int price;
    private String imgUrl;
    private int stockNumber;
    private ItemSellStatus itemSellStatus;
}
