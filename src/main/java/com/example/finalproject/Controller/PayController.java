package com.example.finalproject.Controller;


import com.example.finalproject.entity.ClothesB;
import com.example.finalproject.repository.ClothesBRepository;
import com.example.finalproject.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@Slf4j
public class PayController {

    @Autowired
    PayService payService;
    @Autowired
    ClothesBRepository clothesBRepository;

    @GetMapping("/clothes/basket/{id}")
    public String basketTea(@PathVariable Long id, HttpSession session, Model model,HttpSession httpSession){
        payService.addClothes(id,session);
        String userId = (String) httpSession.getAttribute("userId");
        List<ClothesB> basketList = clothesBRepository.orderUser(userId);
        model.addAttribute("basketList", basketList);
        log.info(String.valueOf(basketList));
        return "mustache/pay/basket";
    }
}
