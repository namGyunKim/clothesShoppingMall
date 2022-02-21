package com.example.finalproject.entity;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity     //DB가 해당 객체를 인식가능하게 만들어줌
public class ClothesG {

    @Id
    private Long id;
    @Column
    private String title;
    @Column
    private String content;
    @Column
    private int price;
    @Column
    private String kate;
    @Column
    private String clothessize;
    @Column
    private String gender;
    @Column
    private int stock;
}
