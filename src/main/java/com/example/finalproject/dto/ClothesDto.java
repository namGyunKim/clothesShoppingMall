package com.example.finalproject.dto;

import com.example.finalproject.entity.Clothes;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ClothesDto {
    private Long id;
    private String title;
    private String content;
    private int price;
    private String kate;
    private String clothessize;
    private String gender;
    private int stock;

    public Clothes toEntity(){
        return new Clothes(id, title, content, price, kate,clothessize,gender,stock);
    }
}
