package com.example.finalproject.Controller;

import com.example.finalproject.entity.Clothes;
import com.example.finalproject.repository.ClothesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class ClothesController {

    @Autowired
    ClothesRepository clothesRepository;


    @RequestMapping("clothes/kate/{kate}")
    public String clothesKate(@PathVariable(value = "kate",required = false)String kate, Model model){
        //        모든 상품 목록을 가져온다
        List<Clothes> clothesList = clothesRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        List<Clothes> kateList=new ArrayList<>();
        for (int i=0;i<clothesList.size();i++){
            if(clothesList.get(i).getKate().equals(kate)){
                kateList.add(clothesList.get(i));
            }
        }
        log.info(String.valueOf(clothesList));
        log.info(String.valueOf(kateList));
        model.addAttribute("kateList", kateList);
        return "mustache/clothes/clothesKate";
    }
}
