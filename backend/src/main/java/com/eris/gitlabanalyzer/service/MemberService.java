package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.Member;
import com.eris.gitlabanalyzer.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> getMembersByProjectId(Long projectId){
        return memberRepository.findByProjectId(projectId);
    }
}
