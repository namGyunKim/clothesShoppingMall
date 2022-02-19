package com.example.finalproject.dto;

import com.example.finalproject.entity.Payrecord;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PayRecordDto {
    private Long id;
    private String userid;
    private String title;
    private int price;
    private Date paydate;

    public Payrecord toEntity(){
        return new Payrecord(id,userid,title,price);
    }
}
