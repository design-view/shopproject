package org.green.shop.controller;

import lombok.RequiredArgsConstructor;
import org.eclipse.jdt.internal.compiler.batch.Main;
import org.green.shop.dto.MainItemDTO;
import org.green.shop.dto.PageRequestDTO;
import org.green.shop.dto.PageResultDTO;
import org.green.shop.service.ItemService;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ItemService itemService;

    @GetMapping("/")
    public String mainPage(Model model, PageRequestDTO requestDTO){
        PageResultDTO<MainItemDTO,Object[]> result= itemService.getMainList(requestDTO);
        model.addAttribute("result",result);
        List<MainItemDTO> list = result.getDtoList();
        for(MainItemDTO dto: list){
            System.out.println(dto.toString());
        }
        return "main";
    }
}
