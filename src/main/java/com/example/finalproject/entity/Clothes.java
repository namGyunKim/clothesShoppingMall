package com.example.finalproject.entity;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity     //DB가 해당 객체를 인식가능하게 만들어줌
public class Clothes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //DB가 알아서 1,2,3 자동생성 어노테이션
    private String id;
    @Column
    private String title;
    @Column
    private String content;
    @Column
    private int price;
    @Column
    private String kate;
}
