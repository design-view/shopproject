package org.green.shop.controller;

import lombok.RequiredArgsConstructor;
import org.green.shop.dto.ItemDTO;
import org.green.shop.dto.MainItemDTO;
import org.green.shop.dto.PageRequestDTO;
import org.green.shop.dto.PageResultDTO;
import org.green.shop.entity.Item;
import org.green.shop.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    @GetMapping("/admin/item/new")
    @CrossOrigin(origins = "http://team4min.jinlabs.net/:80")
    public String itemadd(Model model){
        model.addAttribute("itemDTO",new ItemDTO());
        return "item/itemadd";
    }
    @PostMapping("/admin/item/new")
    @CrossOrigin(origins = "http://team4min.jinlabs.net/:80")
    public String itemadd(ItemDTO itemDTO
            , @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model) throws Exception {
        //첫번째 이미지파일이 없을경우 다시 등록페이지로
        if(itemImgFileList.get(0).isEmpty()){
            model.addAttribute("errorMessage"
                    ,"첫번째 이미지는 필수입니다.");
            return "item/itemadd";
        }
        itemService.saveItem(itemDTO,itemImgFileList);
        return "redirect:/";
    }
    //관리자 상품관리
    @GetMapping("/admin/itemlist")
    @CrossOrigin(origins = "http://team4min.jinlabs.net/:80")
    public String itemList(Model model, PageRequestDTO pageRequestDTO){
        PageResultDTO<ItemDTO, Item> result = itemService.getList(pageRequestDTO);
        model.addAttribute("result",result);
        return "item/itemlist";
    }
    //상품하나조회
    @GetMapping("/admin/item/{itemId}")
    @CrossOrigin(origins = "http://team4min.jinlabs.net/:80")
    public String itemDetail(@PathVariable("itemId") Long itemId, Model model){
       ItemDTO itemDTO = itemService.getItem(itemId);
       model.addAttribute("itemDTO",itemDTO);
       System.out.println(itemDTO.toString());
       return "item/itemdetail";
    }
    //상품수정
    @PostMapping("/admin/item/")
    @CrossOrigin(origins = "http://team4min.jinlabs.net/:80")
    public String itemUpdate(ItemDTO itemDTO
            , @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList)  {
        System.out.println("==============================");
        System.out.println(itemDTO.toString());
        System.out.println("==============================");
        try {
            Long id = itemService.updateItem(itemDTO, itemImgFileList);
        } catch (Exception e) {
            System.out.println("에러에러");
        }
        return "redirect:/";
    }
    //상품상세보기
    @GetMapping("/item/{itemId}")
    @CrossOrigin(origins = "http://team4min.jinlabs.net:80")
    public String itemDetailView(@PathVariable("itemId") Long itemId, Model model){
        ItemDTO itemDTO= itemService.getItem(itemId);
        model.addAttribute("itemDTO",itemDTO);
        return "item/detailView";
    }
    //상품리스트
    @GetMapping("/item/list")
    @CrossOrigin(origins = "http://team4min.jinlabs.net/:80")
    public String itemList(PageRequestDTO pageRequestDTO, Model model){
        PageResultDTO<MainItemDTO,Object[]> result = itemService.getShopList(pageRequestDTO);;
        model.addAttribute("result",result);
        return "item/list";
    }
}
