package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.Member;
import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final GitLabService gitLabService;

    public MemberService(MemberRepository memberRepository, GitLabService gitLabService) {
        this.memberRepository = memberRepository;
        this.gitLabService = gitLabService;
    }

    public void saveMemberInfo(Project project){
        var gitLabMembers = gitLabService.getMembers((project.getId()));
        var gitLabMemberList= gitLabMembers.collectList().block();
        if (gitLabMemberList != null && !gitLabMemberList.isEmpty()) {
            gitLabMemberList.forEach(gitLabMember -> {
                Member member = new Member(
                        gitLabMember.getUsername(),
                        gitLabMember.getName(),
                        project
                );
                memberRepository.save(member);
            });
        }
    }

    public List<Member> getMembersByProjectId(Long projectId){
        return memberRepository.findByProjectId(projectId);
    }
}
