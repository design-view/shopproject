package org.green.shop.service;

import lombok.RequiredArgsConstructor;
import org.green.shop.dto.*;
import org.green.shop.entity.Item;
import org.green.shop.entity.ItemImg;
import org.green.shop.repository.ItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements  ItemService{

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;

    @Override
    public Long saveItem(ItemDTO itemDTO, List<MultipartFile> itemimgFileList)
            throws Exception {
       //item영속저장
        Item item = dtoToEntity(itemDTO);
        itemRepository.save(item);

        //itemImg등록
        for (int i = 0; i < itemimgFileList.size(); i++) {
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if(i==0){
                itemImg.setRepimgYn("Y");
            }else {
                itemImg.setRepimgYn("N");
            }
            itemImgService.saveItemImg(itemImg,itemimgFileList.get(i));
        }
        return item.getId();
    }

    //관리자 상품리스트
    @Override
    public PageResultDTO<ItemDTO, Item> getList(PageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable(Sort.by("id").descending());
        Page<Item> result = itemRepository.findAll(pageable);
        Function<Item,ItemDTO> fn = (entity->entityToDto(entity));
        return new PageResultDTO<>(result,fn);
    }

    //상품상세조회 구현
    @Override
    public ItemDTO getItem(Long itemid) {
        //아이템조회하기
        Optional<Item> result = itemRepository.findById(itemid);
        //result.get() --> Item객체 반환
        //dto객체 생성
        ItemDTO dto = entityToDto(result.get());
        //itemimg리스트 조회
        List<ItemImg> itemimgList = itemImgService.findByItemid(itemid);
        //itemimg엔티티 리스트를 itemImgDTO리스트로 변환
        //리스트 List<ItemImg> -> stream() -> (itemImg -> itemImgDTO) -> List<ItemImgDTO>
        List<ItemImgDTO> itemImgDTOList = itemimgList.stream().map(itemImg ->
                itemImgService.entityToDto(itemImg)).collect(Collectors.toList());
        dto.setItemImgDTOList(itemImgDTOList);
        return dto;
    }

    @Override
    public Long updateItem(ItemDTO itemDTO, List<MultipartFile> itemImgFileList) throws Exception {
        //아이템조회
         Item item = itemRepository.findById(itemDTO.getId()).get();
         //item필드값 변경
         item.updateItem(itemDTO);
         //업데이트
         itemRepository.save(item);

         //itemimg수정
         List<Long> itemImgIds = itemDTO.getItemImgIds();
        for (int i = 0; i < itemImgFileList.size(); i++) {
            if(itemImgIds.get(i)==null){
                System.out.println("널입니다.");
                //엔티티 생성
                ItemImg itemImg = new ItemImg();
                itemImg.setItem(item);
                if(i==0){
                    itemImg.setRepimgYn("Y");
                }else {
                    itemImg.setRepimgYn("N");
                }
                itemImgService.saveItemImg(itemImg,itemImgFileList.get(i));
            }else {
                itemImgService.updateItemImg(itemImgIds.get(i),itemImgFileList.get(i));
            }
        }
        return item.getId();
    }

    @Override
    public PageResultDTO<MainItemDTO, Object[]> getMainList(PageRequestDTO requestDTO) {
        Page<Object[]> result = itemRepository.getListPage(requestDTO.getPageable(
                Sort.by("id").descending()));
        Function<Object[], MainItemDTO> fn = (arr->{
            return entityObjToDTO((Item) arr[0], (ItemImg) arr[1]);
        });
        return new PageResultDTO<>(result,fn);
    }

    @Override
    public PageResultDTO<MainItemDTO, Object[]> getShopList(PageRequestDTO requestDTO) {
        Page<Object[]> result =itemRepository.getList(requestDTO.getPageable(Sort.by("id").descending())
                ,requestDTO.getKeyword());
        Function<Object[], MainItemDTO> fn = (arr->{
            return entityObjToDTO((Item) arr[0], (ItemImg) arr[1]);
        });
        return new PageResultDTO<>(result,fn);
    }
}
