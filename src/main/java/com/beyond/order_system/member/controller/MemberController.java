package com.beyond.order_system.member.controller;

import com.beyond.order_system.common.auth.JwtTokenProvider;
import com.beyond.order_system.member.domain.Member;
import com.beyond.order_system.member.dto.MemberCreateReqDto;
import com.beyond.order_system.member.dto.MemberLoginReqDto;
import com.beyond.order_system.member.dto.TokenResDto;
import com.beyond.order_system.member.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    @Autowired
    public MemberController(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody @Valid MemberCreateReqDto dto){
        Member member = memberService.create(dto);
        return ResponseEntity.status(HttpStatus.OK).body(member.getId());
    }

    @PostMapping("/doLogin")
    public ResponseEntity<?> doLogin(@RequestBody MemberLoginReqDto dto){
        Member member = memberService.login(dto);
        TokenResDto token = TokenResDto.builder()
                .accessToken(jwtTokenProvider.createToken(member))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    @PostMapping("/list")
    public ResponseEntity<?> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    @PostMapping("/myinfo")
    public ResponseEntity<?> findMyInfo(){
        return ResponseEntity.status(HttpStatus.OK).body("");
    }

}
