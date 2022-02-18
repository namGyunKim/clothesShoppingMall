package com.example.finalproject.service;

import com.example.finalproject.dto.ClothesDto;
import com.example.finalproject.entity.Clothes;
import com.example.finalproject.repository.CMemberRepository;
import com.example.finalproject.repository.ClothesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AdminService {
    @Autowired
    CMemberRepository cMemberRepository;
    @Autowired
    ClothesRepository clothesRepository;

    public void addClothes(ClothesDto clothesDto){
        Clothes clothes=clothesDto.toEntity();
        Clothes save =clothesRepository.save(clothes);
        log.info(String.valueOf(save));
    }
}
