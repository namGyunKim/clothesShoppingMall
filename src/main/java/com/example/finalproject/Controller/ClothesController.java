package com.example.finalproject.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ClothesController {

    @RequestMapping("/clothes/new")
    public String newClothes(){
        return "mustache/clothes/new";
    }
}
