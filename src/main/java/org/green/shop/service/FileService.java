package org.green.shop.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
public class FileService {
    //파일등록하기
    public String uploadFile(String uploadPath,String originalFile, byte[] fileData)
            throws Exception {
        //uuid생성 - 중복되지 않도록 파일명을 랜덤으로 만들어줌
        UUID uuid = UUID.randomUUID();
        //확장자 dog.jpg --->  .jpg
        String extension = originalFile.substring(originalFile.lastIndexOf("."));
        //새로운파일명
        String saveFileName = uuid.toString()+extension;  //dfkdjkjfdjddfdf.jpg
        //경로와 파일명 더하기
        String fileUploadFullUrl = uploadPath+"/"+saveFileName;   //shop/item/dfdfdfdd.jpg
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(fileData);
        fos.close();
        return saveFileName;
    }
    //파일삭제하기
    public void deleteFile(String filePath){
        //파읽객체생성
        File deleteFile = new File(filePath);
        //해당 파일이 존재하면 삭제
        if(deleteFile.exists()){
            deleteFile.delete();
        }
    }
}
