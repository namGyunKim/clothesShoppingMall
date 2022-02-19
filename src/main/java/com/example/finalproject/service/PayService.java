package com.example.finalproject.service;


import com.example.finalproject.entity.Clothes;
import com.example.finalproject.entity.ClothesB;
import com.example.finalproject.repository.ClothesBRepository;
import com.example.finalproject.repository.ClothesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
@Slf4j
public class PayService {

    @Autowired
    ClothesRepository clothesRepository;
    @Autowired
    ClothesBRepository clothesBRepository;

    public void addClothes(Long id, HttpSession session){
        String userId=(String)session.getAttribute("userId");
        log.info(userId);
//      entity 생성
        Clothes clothes = clothesRepository.findById(id).orElse(null);
        int price=clothes.getPrice();
        log.info(String.valueOf(clothes));
        //존재한다면 갯수 증가
        if(clothesBRepository.existsById(id)){
            ClothesB clothesB = clothesBRepository.findById(id).orElse(null);
            log.info("개당 가격 : "+price);
            ClothesB clothesBEntity = new ClothesB(id,userId, clothes.getTitle(), clothes.getContent(), clothesB.getPrice()+price,clothes.getKate(),clothesB.getCount()+1);
            log.info(String.valueOf(clothesB));
            log.info(String.valueOf(clothesBEntity));
            ClothesB saved = clothesBRepository.save(clothesBEntity);
            log.info(String.valueOf(saved));
        }
//        장바구니에 존재하지 않는다면 저장
        else {
            ClothesB clothesB = new ClothesB(id,userId, clothes.getTitle(), clothes.getContent(), clothes.getPrice(),clothes.getKate(),1);
            log.info(String.valueOf(clothesB));
            ClothesB saved = clothesBRepository.save(clothesB);
            log.info(String.valueOf(saved));
        }
    }
}
