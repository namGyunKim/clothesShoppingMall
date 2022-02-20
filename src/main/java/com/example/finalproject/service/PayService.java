package com.example.finalproject.service;


import com.example.finalproject.entity.CMember;
import com.example.finalproject.entity.Clothes;
import com.example.finalproject.entity.ClothesB;
import com.example.finalproject.entity.Payrecord;
import com.example.finalproject.repository.CMemberRepository;
import com.example.finalproject.repository.ClothesBRepository;
import com.example.finalproject.repository.ClothesRepository;
import com.example.finalproject.repository.PayRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class PayService {

    @Autowired
    ClothesRepository clothesRepository;
    @Autowired
    ClothesBRepository clothesBRepository;
    @Autowired
    CMemberRepository cMemberRepository;
    @Autowired
    PayRecordRepository payRecordRepository;

    public void addClothes(Long id, HttpSession session, String thisId, RedirectAttributes rttr){
        String userId=(String)session.getAttribute("userId");
        log.info(userId);
//      entity 생성
        Clothes clothes = clothesRepository.findById(id).orElse(null);
        int price=clothes.getPrice();
        log.info(String.valueOf(clothes));
        ClothesB clothesB1111=clothesBRepository.existsClothes(thisId,id);
        log.info("clothesB1111의 값 :"+clothesB1111);
        //존재한다면 이미 추가된상품이라고 알람
        if(clothesB1111 !=null){
            rttr.addFlashAttribute("msg", "이미 장바구니에 추가된 상품입니다");
        }
//        장바구니에 존재하지 않는다면 저장
        else {
            ClothesB clothesB = new ClothesB(null,id,userId, clothes.getTitle(), clothes.getContent(), clothes.getPrice(),clothes.getKate(),1);
            log.info(String.valueOf(clothesB));
            ClothesB saved = clothesBRepository.save(clothesB);
            log.info(String.valueOf(saved));
        }
    }

    public void basketSum(String uid, Model model, Long id,String category){
        //      리스트 가져옴
        List<ClothesB> clothesBList=clothesBRepository.orderUser(uid);
        log.info("해당 아이디의 장바구니 리스트 :"+clothesBList);
        Clothes coffees = clothesRepository.findById(id).orElse(null);
        log.info("해당 상품의 정보 :"+coffees);
        int price=0;
        int price2=coffees.getPrice();
//        처음부터 끝까지 돌려서 해당 유저의 해당 커피id를찾아낸다!
        for(int i=0;i<clothesBList.size();i++){
            if(clothesBList.get(i).getUserid().equals(uid) && clothesBList.get(i).getClothesid().equals(id)){
                price=clothesBList.get(i).getPrice();
                log.info("장바구니 +1 세이브 전");
                ClothesB saved=new ClothesB(clothesBList.get(i).getId(),id,uid, clothesBList.get(i).getTitle(), clothesBList.get(i).getContent(), price + price2,category,clothesBList.get(i).getCount()+1);
                clothesBRepository.save(saved);
                log.info("+1 된 장바구니 상품 정보 :"+saved);
                log.info(String.valueOf(price));
                log.info(String.valueOf(price2));
            }
        }

        model.addAttribute("basketList", clothesBList);
    }

    public void basketDelete(String uid,Model model,Long id,String category,Long basketid){
        //      리스트 가져옴
        List<ClothesB> clothesBList=clothesBRepository.orderUser(uid);
        ClothesB clothesB = clothesBRepository.findById(basketid).orElse(null);
        Clothes clothes = clothesRepository.findById(id).orElse(null);
        int price=0;
        int price2=clothes.getPrice();
        log.info(String.valueOf(clothesB));
        log.info(String.valueOf(clothesBList));
        log.info(String.valueOf(id));
//        해당 상품 -1
        if(clothesB.getPrice() >clothes.getPrice()){
            for(int i=0;i<clothesBList.size();i++){
                if(clothesBList.get(i).getUserid().equals(uid) && clothesBList.get(i).getClothesid().equals(id)){
                    price=clothesBList.get(i).getPrice();
                    ClothesB saved=new ClothesB(clothesBList.get(i).getId(),id,uid, clothesBList.get(i).getTitle(), clothesBList.get(i).getContent(), price - price2,category,clothesBList.get(i).getCount()-1);
                    clothesBRepository.save(saved);
                    log.info(String.valueOf(price));
                    log.info(String.valueOf(price2));
                    log.info(String.valueOf(saved));
                }
            }
        }
        else {
            clothesBRepository.deleteById(basketid);
        }

        model.addAttribute("basketList", clothesBList);
    }

    public void payRecord(String thisId,HttpSession httpSession) {
        List<ClothesB> clothesB = clothesBRepository.orderUser(thisId);
        String titleSum = "";
        int priceSum = 0;
        for (int i = 0; i < clothesB.size(); i++) {
            titleSum += clothesB.get(i).getTitle() + "(" + clothesB.get(i).getCount() + ")    ";
            priceSum += clothesB.get(i).getPrice();
        }
        double discount = discount(httpSession);
        int discount2= (int) (priceSum*discount);
        priceSum=priceSum-discount2;
        LocalDateTime localDateTime = LocalDateTime.now();
        Payrecord payRecord = new Payrecord(null, thisId, titleSum, priceSum, localDateTime);
        log.info(String.valueOf(payRecord));
        payRecordRepository.save(payRecord);
        List<Payrecord> payrecordList = payRecordRepository.orderUser(thisId);
        int grade = 0;
        for (int i = 0; i < payrecordList.size(); i++) {
            grade += payrecordList.get(i).getPrice();
        }
        CMember cMember = cMemberRepository.findById(thisId).orElse(null);
        log.info("지금 로그인중인 멤버 정보 : " + cMember);
        log.info("총 주문 금액: " + grade);
        String thisPassword = cMember.getPassword();
        String thisTel = cMember.getTel();
        String thisAddress = cMember.getAddress();
        String thisAnswer = cMember.getAnswer();
        if (grade >= 5000000) {
            CMember cMember2 = new CMember(thisId, thisPassword, thisTel, thisAddress, "A", thisAnswer);
            log.info(String.valueOf(cMember2));
            cMemberRepository.save(cMember2);
            log.info("A등급");
        } else if (grade >= 500000) {
            CMember cMember2 = new CMember(thisId, thisPassword, thisTel, thisAddress, "B", thisAnswer);
            log.info(String.valueOf(cMember2));
            cMemberRepository.save(cMember2);
            log.info("B등급");
        } else if (grade >= 50000) {
            CMember cMember2 = new CMember(thisId, thisPassword, thisTel, thisAddress, "C", thisAnswer);
            log.info(String.valueOf(cMember2));
            cMemberRepository.save(cMember2);
            log.info("C등급");
        }

    }

    public int clothesBSum(List<ClothesB> clothesBList){
        int sum=0;
        for (int i=0;i<clothesBList.size();i++){
            sum+=clothesBList.get(i).getPrice();
        }
        return sum;
    }

    public double discount(HttpSession httpSession){
        String thisId = (String) httpSession.getAttribute("userId");
        CMember cMember = cMemberRepository.findById(thisId).orElse(null);
        String thisGrade=cMember.getGrade();

        if (thisGrade.equals("A")){
            return 0.2;
        }
        else if(thisGrade.equals("B")){
            return 0.1;
        }
        else if(thisGrade.equals("C")){
            return 0.05;
        }
        else {
            return 0;
        }
    }
}
