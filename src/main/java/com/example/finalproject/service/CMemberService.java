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
//            Repository에게 Entity를 저장하게함
        CMember save = cMemberRepository.save(members);
        log.info(String.valueOf(save));
        msg = "회원가입을 축하드립니다";
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
            strReturn="member/admins";
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


}
