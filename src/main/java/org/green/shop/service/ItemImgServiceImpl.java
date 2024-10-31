package org.green.shop.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.green.shop.entity.ItemImg;
import org.green.shop.repository.ItemImgRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemImgServiceImpl implements ItemImgService {
    @Value("${itemImgLocation}")
    private String itemImgLocation;
    private final FileService fileService;
    private final ItemImgRepository itemImgRepository;
    //파일 업로드, itemImg엔티티 영속저장
    @Override
    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
        //uploadFile(경로, 원본이미지이름, 파일)
        String oriImgName = itemImgFile.getOriginalFilename();
        //파일업로드
        if(!StringUtils.isEmpty(oriImgName)){
            //imgName dog.jpg --->  dfdjfkdjfldjflfjdljfld.jpg
            String imgName = fileService.uploadFile(itemImgLocation,oriImgName
                    ,itemImgFile.getBytes());
            //  /images/item/djddjfdjdfdljfdljf.jpg
            String imgUrl = "/images/item/"+imgName;
            //엔티티필드값 입력
            itemImg.update(oriImgName,imgName,imgUrl);
            //엔티티영속저장
            itemImgRepository.save(itemImg);
        }

    }

    @Override
    public List<ItemImg> findByItemid(Long itemId) {
        List<ItemImg> itemImgList = itemImgRepository.findByItemId(itemId);
        return itemImgList;
    }

    //수정하기
    @Override
    public void updateItemImg(Long itemImgId, MultipartFile multipartFile) throws Exception {
        //itemImgId로 조회
        //있으면 기존이미지 삭제
        //가지고온 이미지 업로드
        //itemImg업데이트
        Optional<ItemImg> result = itemImgRepository.findById(itemImgId);
        ItemImg itemimg  = result.get();
        //이미지 업로드
        String oriImgName = multipartFile.getOriginalFilename();
        System.out.println("수정수정파일이름 " + oriImgName+"이름이름");
        //이미지이름이 비어있지 않으면 파일이름이의 길이가 0보다 클경우만 삭제하기
        if(!StringUtils.isEmpty(itemimg.getImgName()) && oriImgName.length() > 0){
            fileService.deleteFile(itemImgLocation+"/"+itemimg.getImgName());
        }

        if(!StringUtils.isEmpty(oriImgName)) {
            String imgName = fileService.uploadFile(itemImgLocation
                    , oriImgName, multipartFile.getBytes());
            //상품이미지
            String imgUrl = "/images/item/" + imgName;
            itemimg.update(oriImgName, imgName, imgUrl);
            itemImgRepository.save(itemimg);
        }
    }
}
