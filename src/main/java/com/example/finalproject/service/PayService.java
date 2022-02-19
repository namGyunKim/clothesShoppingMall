package com.example.finalproject.service;


import com.example.finalproject.entity.Clothes;
import com.example.finalproject.entity.ClothesB;
import com.example.finalproject.repository.ClothesBRepository;
import com.example.finalproject.repository.ClothesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.util.List;

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

    public void basketSum(String uid, Model model, Long id,String category){
        //      리스트 가져옴
        List<ClothesB> clothesBList=clothesBRepository.orderUser(uid);
        Clothes coffees = clothesRepository.findById(id).orElse(null);
        int price=0;
        int price2=coffees.getPrice();

//        처음부터 끝까지 돌려서 해당 유저의 해당 커피id를찾아낸다!
        for(int i=0;i<clothesBList.size();i++){
            if(clothesBList.get(i).getUserid().equals(uid) && clothesBList.get(i).getId().equals(id)){
                price=clothesBList.get(i).getPrice();
                ClothesB saved=new ClothesB(id,uid, clothesBList.get(i).getTitle(), clothesBList.get(i).getContent(), price + price2,category,clothesBList.get(i).getCount()+1);
                clothesBRepository.save(saved);
                log.info(String.valueOf(price));
                log.info(String.valueOf(price2));
                log.info(String.valueOf(saved));
            }
        }

        model.addAttribute("basketList", clothesBList);
    }

    public void basketDelete(String uid,Model model,Long id,String category){
        //      리스트 가져옴
        List<ClothesB> clothesBList=clothesBRepository.orderUser(uid);
        ClothesB clothesB = clothesBRepository.findById(id).orElse(null);
        Clothes clothes = clothesRepository.findById(id).orElse(null);
        int price=0;
        int price2=clothes.getPrice();
        log.info(String.valueOf(clothesB));
        log.info(String.valueOf(clothesBList));
        log.info(String.valueOf(id));
//        해당 상품 -1
        if(clothesB.getPrice() >clothes.getPrice()){
            for(int i=0;i<clothesBList.size();i++){
                if(clothesBList.get(i).getUserid().equals(uid) && clothesBList.get(i).getId().equals(id)){
                    price=clothesBList.get(i).getPrice();
                    ClothesB saved=new ClothesB(id,uid, clothesBList.get(i).getTitle(), clothesBList.get(i).getContent(), price - price2,category,clothesBList.get(i).getCount()-1);
                    clothesBRepository.save(saved);
                    log.info(String.valueOf(price));
                    log.info(String.valueOf(price2));
                    log.info(String.valueOf(saved));
                }
            }
        }
        else {
            clothesBRepository.deleteById(id);
        }

        model.addAttribute("basketList", clothesBList);
    }

}
