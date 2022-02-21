package com.example.finalproject.Controller;


import com.example.finalproject.entity.CMember;
import com.example.finalproject.entity.ClothesB;
import com.example.finalproject.entity.Payrecord;
import com.example.finalproject.repository.CMemberRepository;
import com.example.finalproject.repository.ClothesBRepository;
import com.example.finalproject.repository.PayRecordRepository;
import com.example.finalproject.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
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
    @Autowired
    CMemberRepository cMemberRepository;

    @GetMapping("/clothes/basket/{id}")
    public String basketTea(@PathVariable Long id, HttpSession session, Model model, HttpSession httpSession, RedirectAttributes rttr) throws UnsupportedEncodingException {
        String thisId = (String) httpSession.getAttribute("userId");
        payService.addClothes(id,session,thisId,rttr);
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
        log.info("gobasket도착");
        String userId = (String) httpSession.getAttribute("userId");
        List<ClothesB> basketList = clothesBRepository.orderUser(userId);
        log.info("basketList :"+basketList);
        model.addAttribute("basketList", basketList);
        int priceSum = payService.clothesBSum(basketList);
        model.addAttribute("priceSum",priceSum);
        double discount= payService.discount(httpSession);
        int discount2= (int) (priceSum*discount);
        int resultSum=priceSum-discount2;
        model.addAttribute("discount", discount2);
        model.addAttribute("resultSum", resultSum);
        log.info("gobasket에서 이동하기 직전");
        return "mustache/pay/basket";
    }
    @GetMapping("/clothes/basket/add/{uid}/{id}/{category}")
    public String add(@PathVariable String uid,Model model,@PathVariable Long id,
                      @PathVariable String category){
        log.info("장바구니 +1");
        payService.basketSum(uid, model, id,category);
        return "redirect:/go/basket";
    }

    @GetMapping("/clothes/basket/delete/{uid}/{id}/{category}/{basketid}")
    public String delete(@PathVariable String uid,Model model,@PathVariable Long id,
                         @PathVariable String category,
                         @PathVariable Long basketid){
        payService.basketDelete(uid, model, id,category,basketid);
        return "redirect:/go/basket";
    }

    @RequestMapping("/payrecord")
    public String payRecord(HttpSession httpSession,Model model){
        String thisId = (String) httpSession.getAttribute("userId");
        payService.payRecord(thisId,httpSession);
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

    @RequestMapping("/kakaopay")
    public String kakaoPay(String uid,HttpServletResponse response, HttpSession httpSession){
        String thisId = (String) httpSession.getAttribute("userId");
        List<ClothesB> clothesB = clothesBRepository.orderUser(thisId);
        int priceSum=0;
        for (int i=0;i<clothesB.size();i++){
            priceSum+=clothesB.get(i).getPrice();
        }
        double discount= payService.discount(httpSession);
        int discount2= (int) (priceSum*discount);
        priceSum= priceSum-discount2;
//        userId세션 받아옴
        String payUserId= (String) httpSession.getAttribute("userId");


        //userid쿠키생성
        Cookie cookie2=new Cookie("userID",String.valueOf(payUserId));
        cookie2.setMaxAge(60*60*24);
        response.addCookie(cookie2);

        //쿠키생성
        Cookie cookie=new Cookie("totalsum3",String.valueOf(priceSum));
        cookie.setMaxAge(60*60*24);
        response.addCookie(cookie);
        return "mustache/pay/kakao";
    }

}
