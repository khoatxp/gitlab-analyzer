package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.Member;
import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.repository.MemberRepository;
import com.eris.gitlabanalyzer.repository.ProjectRepository;
import com.eris.gitlabanalyzer.repository.ServerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final ServerRepository serverRepository;
    private final GitLabService gitLabService;

    @Value("${gitlab.SERVER_URL}")
    String serverUrl;

    @Value("${gitlab.ACCESS_TOKEN}")
    String accessToken;

    public MemberService(MemberRepository memberRepository, ProjectRepository projectRepository,ServerRepository serverRepository,GitLabService gitLabService) {
        this.memberRepository = memberRepository;
        this.projectRepository = projectRepository;
        this.serverRepository = serverRepository;
        this.gitLabService = gitLabService;
    }

    public void saveMemberInfo(Long gitLabProjectId){
        Project project = projectRepository.findByGitlabProjectIdAndServerUrl(gitLabProjectId, serverUrl);

        var gitLabMembers = gitLabService.getMembers(gitLabProjectId);
        var gitLabMemberList= gitLabMembers.collectList().block();

        if (gitLabMemberList != null && !gitLabMemberList.isEmpty()) {
            gitLabMemberList.forEach(gitLabMember -> {
                Member member = memberRepository.findByUserNameAndServerUrl(gitLabMember.getUsername(),serverUrl);
                if (member == null){
                    member = new Member(
                            gitLabMember.getUsername(),
                            gitLabMember.getName(),
                            serverRepository.find(serverUrl,accessToken)
                    );
                }
                member.addProject(project);
                memberRepository.save(member);
            });
        }
    }

    public List<Member> getMembersByProjectId(Long projectId){
        return memberRepository.findByProjectId(projectId);
    }
}
