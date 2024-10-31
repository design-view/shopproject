package org.green.shop.entity;

import jakarta.persistence.*;
import lombok.*;
import org.green.shop.constant.ItemSellStatus;
import org.green.shop.dto.ItemDTO;
import org.green.shop.exception.OutOfStockException;

@Entity
@Table(name="item")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@SequenceGenerator(name="my_item_seq", sequenceName = "item_seq", allocationSize = 1)
public class Item {
    @Id
    @Column(name="item_id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "my_item_seq")
    private Long id;                        //상품번호 기본키
    @Column(nullable = false, length = 300)
    private String itemNm;                  //상품명
    @Column(nullable = false)
    private int price;                      //가격
    @Column(nullable = false)
    private int stockNumber;                //재고수량
    @Lob  //BLOB타입 매핑
    @Column(nullable = false)
    private String itemDetail;              //상세설명
    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;  //상품판매상태   SELL, SOLD_OUT

    public void updateItem(ItemDTO dto){
        this.itemNm = dto.getItemNm();
        this.itemDetail = dto.getItemDetail();
        this.price = dto.getPrice();
        this.stockNumber = dto.getStockNumber();
        this.itemSellStatus = dto.getItemSellStatus();
    }
    //상품의 재고를 주문수량만큼 감소시키기
    public void removeStock(int stockNumber){
        int restStock = this.stockNumber - stockNumber;
        //주문수량이 재고수량보타 클때 (현재 재고수량에서 주문수량을 뺀값이 -일때)
        if(restStock<0){
            //예외발생
            throw new OutOfStockException("상품의 재고가 부족합니다.(현재 재고 수량: "
                    +this.stockNumber+")");
        }
        this.stockNumber =restStock;
    }
    //주문취소시 주문수량만큼 상품의 재고를 더하기
    public void addStock(int stockNumber){
        this.stockNumber += stockNumber;
    }





}
