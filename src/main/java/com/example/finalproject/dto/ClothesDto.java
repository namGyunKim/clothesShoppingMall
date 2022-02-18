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

    public Clothes toEntity(){
        return new Clothes(id, title, content, price, kate);
    }
}
