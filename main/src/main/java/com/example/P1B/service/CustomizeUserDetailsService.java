package com.example.P1B.service;

import com.example.P1B.domain.Member;
import com.example.P1B.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomizeUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(
                () -> new UsernameNotFoundException("해당 아이디를 찾을 수 없습니다 : " + memberId)
        );
        return new CustomizeUserDetails(member);
    }
}