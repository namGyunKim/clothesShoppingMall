package com.example.finalproject.service;

import com.example.finalproject.dto.CMemberDto;
import com.example.finalproject.entity.CMember;
import com.example.finalproject.repository.CMemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
@Slf4j
public class CMemberService {

    @Autowired
    CMemberRepository cMemberRepository;

    public void MemberIdCheck(CMemberDto dto, String password2, HttpSession httpSession){
        //      전체 멤버 조회
        List<CMember> membersList = cMemberRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        log.info(String.valueOf(membersList));
        log.info(String.valueOf(dto));
        log.info(password2);
//      만약 중복되는 아이디가 있다면
        if(cMemberRepository.existsById(dto.getId())){
            httpSession.setAttribute("rttrMsg","중복되는 아이디가 있습니다");
            log.info("아이디가 중복됨");
        }
        else {
            if (dto.getPassword().equals(password2)){

                httpSession.setAttribute("rttrMsg","회원가입이 가능합니다");
                log.info("아이디가 중복안됨");
            }
            else{
                httpSession.setAttribute("rttrMsg","비밀번호가 일치하지않습니다");
            }
        }
    }

    public boolean SessionExist(String str,HttpSession httpSession){
        if (httpSession.getAttribute(str)!=null) return true;
        else return false;
    }

    public String MemberJoin(CMemberDto dto) {
        String msg;
//            dto를 Entity로 변환
        CMember members = dto.toEntity();
        log.info(String.valueOf(dto));
        log.info(String.valueOf(members));

        if(cMemberRepository.existsById(dto.getId())){
            msg = "회원정보 수정 완료";
        }
        else {
            msg = "회원가입을 축하드립니다";

        }

//            Repository에게 Entity를 저장하게함
        CMember save = cMemberRepository.save(members);
        log.info(String.valueOf(save));
        return msg;
    }
    public String MemberLogin(Model model, CMemberDto dto, HttpSession session) {
        String msg;
        String adminsId = "admin@admin.com";
        String adminsPassword = "1234";
        String strReturn;
        //        관리자 계정이면
        if (dto.getId().equals(adminsId) && dto.getPassword().equals(adminsPassword)) {
            session.setAttribute("admin", dto.getId());
            session.setAttribute("userId", dto.getId());
            strReturn="mustache/admin/index";
        } else if (cMemberRepository.existsById(dto.getId())) {
            CMember membersEntity = cMemberRepository.findById(dto.getId()).orElse(null);
            log.info(String.valueOf(membersEntity));
//            알람창추가위한것
            if (membersEntity.getPassword().equals(dto.getPassword())) {

                log.info(String.valueOf(dto));
//                로그인 세션 추가
                session.setAttribute("loginMsg", "ok");
                session.setAttribute("userId", dto.getId());
                session.setAttribute("userPassword", dto.getPassword());
                strReturn="redirect:/";
            } else {
                session.setAttribute("rttrMsg","비밀번호가 틀렸습니다");
                log.info(String.valueOf(dto));
                strReturn="redirect:/login";
            }
        } else {
            session.setAttribute("rttrMsg","아이디가 틀렸습니다");
            log.info(String.valueOf(dto));
            strReturn="redirect:/login";
        }
        return strReturn;
    }
    public void MemberDelete(String id,HttpSession session){
        //        삭제할 데이터 가져오기
        CMember membersEntity = cMemberRepository.findById(id).orElse(null);
        log.info(String.valueOf(membersEntity));
//        데이터 삭제
        cMemberRepository.deleteById(id);
//        세션 삭제
        session.removeAttribute("userId");
        session.removeAttribute("userPassword");
        session.removeAttribute("admin");
        session.setAttribute("deleteMsg","ok");
    }

    public void MemeberPasswordCheck(String id,String password,HttpSession httpSession){
        CMember cMember = cMemberRepository.findById(id).orElse(null);
        if(cMember.getPassword().equals(password)){
            httpSession.setAttribute("passCheck","ok");
        }
        else {
            httpSession.removeAttribute("passCheck");
        }
    }

    public void MemberEdit(Model model,HttpSession httpSession){
        String thisId = (String) httpSession.getAttribute("userId");
        CMember cMember = cMemberRepository.findById(thisId).orElse(null);
        model.addAttribute("memberInfo", cMember);
        String address = cMember.getAddress();
        String[] array = address.split("    ");
        model.addAttribute("address1",array[0]);
        model.addAttribute("address2",array[1]);
        model.addAttribute("address3",array[2]);
        log.info(array[0]);
        log.info(array[1]);
        log.info(array[2]);
    }

    public boolean idCheck(String id){
        boolean idCheck=cMemberRepository.existsById(id);
        if(idCheck){
            log.info(id+"는 존재하는 아이디");
            return true;
        }
        else{
            log.info(id+"는 존재하지 않는 아이디");
            return false;
        }
    }
}
