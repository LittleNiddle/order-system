package com.beyond.order_system.member.service;

import com.beyond.order_system.member.domain.Member;
import com.beyond.order_system.member.dto.MemberCreateReqDto;
import com.beyond.order_system.member.dto.MemberLoginReqDto;
import com.beyond.order_system.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Member create(MemberCreateReqDto dto){
        if(memberRepository.findByEmail(dto.getEmail()).isPresent()){
            throw new IllegalArgumentException("중복되는 이메일 입니다.");
        }

        Member member = dto.toEntity(passwordEncoder.encode(dto.getPassword()));
        memberRepository.save(member);
        return member;
    }

    public Member login(MemberLoginReqDto dto){
        Optional<Member> optionalMember = memberRepository.findByEmail(dto.getEmail());
        boolean check = true;
        if(optionalMember.isEmpty()){
            check = false;
        } else{
            if(!passwordEncoder.matches(dto.getPassword(), optionalMember.get().getPassword())) check = false;
        }
        if(!check){
            throw new IllegalArgumentException("email 또는 password가 일치하지 않습니다.");
        }
        return optionalMember.get();
    }

}
