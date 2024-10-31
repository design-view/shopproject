package org.green.shop.repository;

import com.querydsl.core.BooleanBuilder;
import org.green.shop.constant.ItemSellStatus;
import org.green.shop.entity.Item;
import org.green.shop.entity.QItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
public class ItemRepositoryTests {
    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("상품 저장 테스트")
    public void createItemTest(){
        IntStream.rangeClosed(1,30).forEach(i->{
            Item item = Item.builder()
                    .itemNm("테스트 상품"+i)
                    .price(10000+i)
                    .itemDetail("테스트 상품 상세"+i)
                    .itemSellStatus(ItemSellStatus.SELL)
                    .stockNumber(100)
                    .build();
            itemRepository.save(item);
        });
    }
    @Test
    @DisplayName("상품명 조회테스트")
    public void findByItemNmTest(){
        List<Item> result = itemRepository.findByItemNm("테스트 상품1");
        result.forEach(item->{
            System.out.println(item.toString());
        });
    }
    @Test
    @DisplayName("상품명,상품상세설명 or테스트")
    public void findByItemNmOrItemDetailTest(){
        List<Item> result = itemRepository.findByItemNmOrItemDetail("테스트 상품1",
                "테스트 상품 상세5");
        result.forEach(item->{
            System.out.println(item.toString());
        });
    }
    @Test
    @DisplayName("가격 lessthan테스트")
    public void findByPriceLessThanTest(){
        List<Item> result = itemRepository.findByPriceLessThan(10005);
        result.forEach(item->{
            System.out.println(item.toString());
        });
    }
    @Test
    @DisplayName("가격 내림차순 테스트")
    public void findByPriceLessThanOrderByTest(){
        List<Item> result = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
        result.forEach(item->{
            System.out.println(item.toString());
        });
    }
    @Test
    @DisplayName("@query를 이용한 조회테스트")
    public void fineByItemDetailTest(){
        List<Item> result = itemRepository.findByItemDeail("테스트");
        result.forEach(item->{
            System.out.println(item.toString());
        });
    }
    @Test
    @DisplayName("상품 Querydsl 조회 테스트")
    public void queryDslTest1(){
        //쿼리에 들어갈 조건을 만들어주는 빌더
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QItem item = QItem.item;
        String itemDetail = "테스트 상품 상세";
        int price = 10005;
        String itemSellStat = "SELL";

        //where절에  itemDetial like "%테스트 상품 상세%"
        booleanBuilder.and(item.itemDetail.like("%"+itemDetail+"%"));
        //10005보다 가격이 높은 항목 조회
        booleanBuilder.and(item.price.gt(price));
        //판매상태가 SELL인 항목 조회
        booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
        Pageable pageable = PageRequest.of(0,10, Sort.by("id").descending());
        Page<Item> itemPaging = itemRepository.findAll(booleanBuilder,pageable);
        List<Item> items = itemPaging.getContent();
        for(Item i: items){
            System.out.println(i.toString());
        }
    }
    @Test
    public void mainList(){
        Pageable pageable = PageRequest.of(0,10
                ,Sort.by("id").descending());
        Page<Object[]> result = itemRepository.getListPage(pageable);
        List<Object[]> lists =  result.getContent();
        for (Object[] obj: lists){
            System.out.println(Arrays.toString(obj));
        }
    }
    @Test
    public void mallList(){
        Pageable pageable = PageRequest.of(0,3
                ,Sort.by("id").descending());
        Page<Object[]> result = itemRepository.getList(pageable,"");
        List<Object[]> lists = result.getContent();
        for (Object[] obj: lists){
            System.out.println(Arrays.toString(obj));
        }
    }
}
