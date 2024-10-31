package org.green.shop.dto;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class CartOrderDTO {
    private Long cartItemId;
    private List<CartOrderDTO> cartOrderDtoList;
}
