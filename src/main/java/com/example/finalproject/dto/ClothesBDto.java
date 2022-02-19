package com.example.finalproject.dto;

import com.example.finalproject.entity.Clothes;
import com.example.finalproject.entity.ClothesB;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ClothesBDto {
    private Long id;
    private String userid;
    private String title;
    private String content;
    private int price;
    private String kate;
    private int count;

    public ClothesB toEntity(){
        return new ClothesB(id,userid, title, content, price, kate,count);
    }
}
