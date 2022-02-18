package com.example.finalproject.Controller;

import com.example.finalproject.entity.CMember;
import com.example.finalproject.repository.CMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    CMemberRepository cMemberRepository;

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

}
