package com.example.finalproject.Controller;


import com.example.finalproject.entity.ClothesB;
import com.example.finalproject.entity.Payrecord;
import com.example.finalproject.repository.ClothesBRepository;
import com.example.finalproject.repository.PayRecordRepository;
import com.example.finalproject.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@Slf4j
public class PayController {

    @Autowired
    PayService payService;
    @Autowired
    ClothesBRepository clothesBRepository;
    @Autowired
    PayRecordRepository payRecordRepository;

    @GetMapping("/clothes/basket/{id}")
    public String basketTea(@PathVariable Long id, HttpSession session, Model model,HttpSession httpSession) throws UnsupportedEncodingException {
        payService.addClothes(id,session);
        String userId = (String) httpSession.getAttribute("userId");
        String category = (String) httpSession.getAttribute("category");
        log.info("category의 값은 : "+category);
        String encodedParam = URLEncoder.encode(category, "UTF-8");
        List<ClothesB> basketList = clothesBRepository.orderUser(userId);
        model.addAttribute("basketList", basketList);
        log.info(String.valueOf(basketList));
        return "redirect:/clothes/kate/"+encodedParam;
    }

    @RequestMapping("/go/basket")
    public String goBasket(HttpSession httpSession,Model model){
        String userId = (String) httpSession.getAttribute("userId");
        List<ClothesB> basketList = clothesBRepository.orderUser(userId);
        model.addAttribute("basketList", basketList);
        return "mustache/pay/basket";
    }
    @GetMapping("/clothes/basket/add/{uid}/{id}/{category}")
    public String add(@PathVariable String uid,Model model,@PathVariable Long id,
                      @PathVariable String category){
        payService.basketSum(uid, model, id,category);
        return "redirect:/go/basket";
    }

    @GetMapping("/clothes/basket/delete/{uid}/{id}/{category}")
    public String delete(@PathVariable String uid,Model model,@PathVariable Long id,
                         @PathVariable String category){
        payService.basketDelete(uid, model, id,category);
        return "redirect:/go/basket";
    }

    @RequestMapping("/payrecord")
    public String payRecord(HttpSession httpSession,Model model){
        String thisId = (String) httpSession.getAttribute("userId");
        List<ClothesB> clothesB = clothesBRepository.orderUser(thisId);
        String titleSum = "";
        int priceSum=0;
        for (int i=0;i<clothesB.size();i++){
            titleSum+=clothesB.get(i).getTitle()+"    ";
            priceSum+=clothesB.get(i).getPrice();
        }
        LocalDateTime localDateTime=LocalDateTime.now();
        Payrecord payRecord = new Payrecord(null, thisId, titleSum, priceSum,localDateTime);
        log.info(String.valueOf(payRecord));
        payRecordRepository.save(payRecord);
        return "mustache/index";
    }

    @RequestMapping("/info/payrecord")
    public String infoPayRecord(HttpSession httpSession,Model model){

        String thisId = (String) httpSession.getAttribute("userId");
        List<Payrecord> payrecordList = payRecordRepository.orderUser(thisId);
        model.addAttribute("payrecordList", payrecordList);
        log.info("주문 내역 : "+String.valueOf(payrecordList));
        return "mustache/pay/payrecord";
    }

}
