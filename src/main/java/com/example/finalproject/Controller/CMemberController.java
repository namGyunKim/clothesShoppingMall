package com.example.finalproject.Controller;

import com.example.finalproject.dto.CMemberDto;
import com.example.finalproject.entity.CMember;
import com.example.finalproject.repository.CMemberRepository;
import com.example.finalproject.service.CMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
@Slf4j
public class CMemberController {
    @Autowired
    CMemberRepository cMemberRepository;
    @Autowired
    CMemberService cMemberService;

    @RequestMapping("/")
    public String index(HttpSession httpSession){
        httpSession.removeAttribute("passCheck");
        return "mustache/index";
    }

    @GetMapping("/new")
    public String newJoin(){
        return "mustache/member/newJoin";
    }

    @PostMapping("/new/idcheck")
    public String newIdCheck(CMemberDto dto, @RequestParam(value = "password2",required = false) String password2, RedirectAttributes rttr, HttpSession httpSession){
        cMemberService.MemberIdCheck(dto,password2,httpSession);
        if (cMemberService.SessionExist("rttrMsg",httpSession)){
            String strSession = (String) httpSession.getAttribute("rttrMsg");
            rttr.addFlashAttribute("msg", strSession);
            httpSession.removeAttribute("rttrMsg");
            if (strSession.equals("회원가입이 가능합니다")){
                rttr.addFlashAttribute("idcheck", dto.getId());
                rttr.addFlashAttribute("password", dto.getPassword());
                rttr.addFlashAttribute("tel", dto.getTel());
                rttr.addFlashAttribute("address", dto.getAddress());
                rttr.addFlashAttribute("answer", dto.getAnswer());
            }
        }
        log.info("dto 의 정보"+String.valueOf(dto));
        return "redirect:/new";

    }

    @PostMapping("/new2")
    public String createMember(CMemberDto dto, RedirectAttributes rttr,
                               @RequestParam(value = "address1",required = false) String address1,
                               @RequestParam(value = "address2",required = false) String address2,
                               @RequestParam(value = "address3",required = false) String address3){
        String address=address1+"    "+address2+"    "+address3;
        dto.setAddress(address);
        log.info("회원가입할때 입력한 정보 :"+String.valueOf(dto));
        String msg=cMemberService.MemberJoin(dto);
        rttr.addFlashAttribute("msg", msg);
        return  "redirect:/";
    }

    @GetMapping("/login")
    public String login(Model model, HttpServletResponse response, HttpSession session) throws Exception{

        String deleteMsg = (String) session.getAttribute("deleteMsg");
        log.info("deleteMSg :  "+deleteMsg);
        if (deleteMsg==null){
            log.info("deleteMsg :"+deleteMsg);
        }
        else if (deleteMsg.equals("ok")){
            cMemberService.alertMsg("회원탈퇴 되었습니다.",response);
            session.removeAttribute("deleteMsg");
        }

        return "mustache/member/login";
    }

    @PostMapping("/login2")
    public String login2(Model model, CMemberDto dto, RedirectAttributes rttr, HttpSession session) {

        String strReturn=cMemberService.MemberLogin(model,dto, session);
//        rttrMsg세션이 존재한다면
        if(cMemberService.SessionExist("rttrMsg",session)){
            rttr.addFlashAttribute("msg", session.getAttribute("rttrMsg"));
            session.removeAttribute("rttrMsg");
        }
        return strReturn;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session,RedirectAttributes rttr){

        session.removeAttribute("userId");
        session.removeAttribute("userPassword");
        session.removeAttribute("admin");
        rttr.addFlashAttribute("msg", "로그아웃 되었습니다");

        return "redirect:/login";
    }

    @RequestMapping("/member/info")
    public String memberInfo(HttpSession httpSession,Model model,@RequestParam(value = "password",required = false) String password){
        String thisId = (String) httpSession.getAttribute("userId");
        cMemberService.MemeberPasswordCheck(thisId,password,httpSession);
        log.info("현재 정보 수정하려는 유저 아이디는 :"+thisId);
        CMember cMember = cMemberRepository.findById(thisId).orElse(null);
        model.addAttribute("memberInfo", cMember);
        return "mustache/member/info";
    }
    @GetMapping("/go/delete")
    public String goDelete(){
        return "mustache/member/delete";
    }

    @GetMapping("/member/delete/{id}")
    public String delete(@PathVariable String id,HttpSession session){
        cMemberService.MemberDelete(id,session);
        return "redirect:/login";
    }

    @RequestMapping("/member/edit")
    public String memberEdit(Model model,HttpSession httpSession){
        cMemberService.MemberEdit(model,httpSession);
        return "mustache/member/edit";
    }

    @RequestMapping("/member/passwordFind")
    public String passwordFind(){
        return "mustache/member/passwordFind";
    }

    @RequestMapping("/id/check")
    public String idCheck(@RequestParam(value = "id",required = false) String id,
                          RedirectAttributes rttr,
                          Model model,
                          HttpServletResponse response) throws IOException {
        boolean idCheck= cMemberService.idCheck(id);

        if(idCheck){
            model.addAttribute("findId", id);
            return "mustache/member/answer";
        }
        else {
            cMemberService.alertMsg("존재하지 않는 아이디 입니다.",response);

            return "mustache/member/passwordFind";
        }
    }

    @RequestMapping("/answer/check")
    public String answerCheck(@RequestParam(value = "id",required = false) String id,
                              @RequestParam(value = "answer",required = false) String answer,
                          RedirectAttributes rttr,
                          Model model,
                          HttpServletResponse response) throws IOException {
        boolean answerCheck = cMemberService.answerCheck(id, answer);

        if(answerCheck){
            model.addAttribute("findId", id);
            return "mustache/member/passwordSet";
        }
        else {
            model.addAttribute("findId", id);
            cMemberService.alertMsg("답변이 일치하지 않습니다.",response);

            return "mustache/member/answer";
        }
    }

    @RequestMapping("/passwordSet")
    public String passwordSet(CMemberDto cMemberDto,HttpServletResponse response) throws IOException {
        CMember cMember = cMemberRepository.findById(cMemberDto.getId()).orElse(null);
        cMember.setPassword(cMemberDto.getPassword());
        cMemberRepository.save(cMember);
        log.info(String.valueOf(cMember));
        cMemberService.alertMsg("비밀번호가 변경되었습니다.",response);
        return "mustache/index";
    }

}
