package com.example.finalproject.Controller;

import com.example.finalproject.repository.ClothesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ClothesController {

    @Autowired
    ClothesRepository clothesRepository;


    @RequestMapping("clothes/kate/{kate}")
    public String clothesKate(@PathVariable(value = "kate",required = false)String kate){


        return "mustache/clothes/clothesKate";
    }
}
