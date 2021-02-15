package com.eris.gitlabanalyzer.controller;

import com.eris.gitlabanalyzer.model.Member;
import com.eris.gitlabanalyzer.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MemberController {
    private final MemberService memberService;
    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/api/v1/members")
    public List<Member> getMembersByProjectId(@RequestParam(required = true) String projectId){
        return memberService.getMembersByProjectId(Long.parseLong(projectId));
    }
}
