package com.example.finalproject.Controller;

import com.example.finalproject.dto.ClothesDto;
import com.example.finalproject.entity.CMember;
import com.example.finalproject.entity.Clothes;
import com.example.finalproject.repository.CMemberRepository;
import com.example.finalproject.repository.ClothesRepository;
import com.example.finalproject.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Slf4j
public class AdminController {

    @Autowired
    CMemberRepository cMemberRepository;
    @Autowired
    ClothesRepository clothesRepository;
    @Autowired
    AdminService adminService;

    @GetMapping("/toadmin")
    public String membersEdit(){
        return "mustache/admin/index";
    }

    @RequestMapping("/member/view")
    public String membersView(Model model){
//        모든 members목록을 가져온다
        List<CMember> membersList = cMemberRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
//        가져온 Members묶음을 뷰로 전달할 변수 설정
        model.addAttribute("membersList", membersList);
//        뷰 페이지 설정
        return "mustache/admin/memberAll";
    }

    @RequestMapping("/clothes/new")
    public String newClothes(){
        return "mustache/admin/newClothes";
    }

    @RequestMapping("/clothes/new2")
    public String newClothes2(ClothesDto clothesDto, RedirectAttributes rttr){
        adminService.addClothes(clothesDto);
        rttr.addFlashAttribute("msg","상품등록 완료");
      return "redirect:/clothes/new";
    }
    @RequestMapping("/clothes/index")
    public String clothesIndex(Model model){
        List<Clothes> clothesList = clothesRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        model.addAttribute("clothesList", clothesList);
        return "mustache/admin/clothesAllView";
    }

}
