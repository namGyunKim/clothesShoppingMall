package com.example.finalproject.dto;

import com.example.finalproject.entity.CMember;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CMemberDto {
    private String id;
    private String password;
    private String tel;
    private String address;
    private String grade;
    private String answer;

    public CMember toEntity(){
        return new CMember(id,password,tel,address,grade,answer);
    }
}
