package com.example.springjwt.controller;

import com.example.springjwt.Service.JoinService;
import com.example.springjwt.dto.JoinDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class JoinController {

    private final JoinService joinService;

    public JoinController(JoinService joinService) {

        this.joinService = joinService;
    }


    @PostMapping("/join")
    public String joinProcess(JoinDTO joinDTO) {

        joinService.JoinProcess(joinDTO);
        //JoinDTO는 클래스 이름으로 이미 메서드 파라미터로 JoinDTO joinDTO를 이미 받고 있으니, 대문자 x / 소문자로 객체를 넘겨주어야함


        return "ok";
    }
}
