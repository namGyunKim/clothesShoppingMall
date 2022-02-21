package com.example.finalproject.service;

import com.example.finalproject.dto.ClothesDto;
import com.example.finalproject.entity.Clothes;
import com.example.finalproject.entity.ClothesG;
import com.example.finalproject.repository.CMemberRepository;
import com.example.finalproject.repository.ClothesGRepository;
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
    @Autowired
    ClothesGRepository clothesGRepository;

    public void addClothes(ClothesDto clothesDto){
        Clothes clothes=clothesDto.toEntity();
        Clothes save =clothesRepository.save(clothes);
        log.info(String.valueOf(save));
    }

    public void CoffeeKateDelete(Long id){
        //        삭제할 데이터 가져오기
        Clothes clothes=clothesRepository.findById(id).orElse(null);
        log.info(String.valueOf(clothes));
//        상품 삭제
        clothesRepository.deleteById(id);
//        휴지통에 추가
        ClothesG clothesG = new ClothesG(id, clothes.getTitle(), clothes.getContent(), clothes.getPrice(),clothes.getKate(),clothes.getClothessize(),clothes.getGender(),clothes.getStock());
        log.info(String.valueOf(clothesG));
        ClothesG saved = clothesGRepository.save(clothesG);
    }

    public void recovery(Long id){
//      복구할 데이터 가져오기
        ClothesG clothesG = clothesGRepository.findById(id).orElse(null);
        log.info(String.valueOf(clothesG));
//        휴지통에서 삭제
        clothesGRepository.deleteById(id);
//        상품 목록에 복구
        Clothes clothes = new Clothes(id, clothesG.getTitle(), clothesG.getContent(), clothesG.getPrice(),clothesG.getKate(),clothesG.getClothessize(),clothesG.getGender(),clothesG.getStock());
        log.info(String.valueOf(clothes));
        Clothes saved = clothesRepository.save(clothes);
    }

}
