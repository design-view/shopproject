package org.green.shop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.nio.channels.Pipe;

@Entity
@Table(name="item_img")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@SequenceGenerator(name="itemimg_seq" ,sequenceName = "itemimg_seq",allocationSize = 1)
public class ItemImg {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "itemimg_seq")
    @Column(name="item_img_id")
    private Long id;
    private String imgName;                //이미지이름
    private String oriImgName;             //원본이미지이름
    private String imgUrl;                 //경로
    private String repimgYn;               //대표이미지여부
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;                     //다대일관계

    public void update(String oriImgName, String imgName, String imgUrl){
        this.imgName = imgName;
        this.imgUrl = imgUrl;
        this.oriImgName = oriImgName;
    }
}
