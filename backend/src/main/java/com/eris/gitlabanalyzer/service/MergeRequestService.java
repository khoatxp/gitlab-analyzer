package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.Member;
import com.eris.gitlabanalyzer.model.MergeRequest;
import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.repository.MemberRepository;
import com.eris.gitlabanalyzer.repository.MergeRequestRepository;
import com.eris.gitlabanalyzer.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MergeRequestService {
    GitLabService gitLabService;
    MergeRequestRepository mergeRequestRepository;
    ProjectRepository projectRepository;
    MemberRepository memberRepository;

    @Value("${gitlab.SERVER_URL}")
    String serverUrl;

    @Value("${gitlab.ACCESS_TOKEN}")
    String accessToken;

    public MergeRequestService(GitLabService gitLabService, MergeRequestRepository mergeRequestRepository, ProjectRepository projectRepository, MemberRepository memberRepository) {
        this.gitLabService = gitLabService;
        this.mergeRequestRepository = mergeRequestRepository;
        this.projectRepository = projectRepository;
        this.memberRepository = memberRepository;
    }

    public void saveMergeRequestInfo(Long gitLabProjectId){
        Project project = projectRepository.findByGitlabProjectIdAndServerUrl(gitLabProjectId, serverUrl);

        var gitLabMergeRequests = gitLabService.getMergeRequests(gitLabProjectId);
        var gitLabMergeRequestList = gitLabMergeRequests.collectList().block();

        if (gitLabMergeRequestList != null && !gitLabMergeRequestList.isEmpty()){
            gitLabMergeRequestList.forEach(gitLabMergeRequest -> {
                    Member member = memberRepository.findByUserName(gitLabMergeRequest.getUsername());
                    MergeRequest mergeRequest = new MergeRequest(
                            gitLabMergeRequest.getName(),
                            gitLabMergeRequest.getUsername(),
                            gitLabMergeRequest.getTitle(),
                            gitLabMergeRequest.getDescription(),
                            gitLabMergeRequest.getCreatedAt(),
                            gitLabMergeRequest.getWebUrl(),
                            project,
                            member
                    );
                    mergeRequestRepository.save(mergeRequest);
                }
            );
        }

    }
}
