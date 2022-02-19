package com.example.finalproject.entity;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity     //DB가 해당 객체를 인식가능하게 만들어줌
public class ClothesB {

    @Id
    private Long id;
    @Column
    private String userid;
    @Column
    private String title;
    @Column
    private String content;
    @Column
    private int price;
    @Column
    private String kate;
    @Column
    private int count;
}
