package org.green.shop.dto;

import lombok.Data;

@Data
public class CartItemDTO {
    private Long itemId;
    private int count;
}
