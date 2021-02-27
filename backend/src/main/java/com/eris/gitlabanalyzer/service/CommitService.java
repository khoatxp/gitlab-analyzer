package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.*;
import com.eris.gitlabanalyzer.model.gitlabresponse.GitLabCommit;
import com.eris.gitlabanalyzer.repository.CommitRepository;
import com.eris.gitlabanalyzer.repository.GitManagementUserRepository;
import com.eris.gitlabanalyzer.repository.MergeRequestRepository;
import com.eris.gitlabanalyzer.repository.ProjectRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommitService {

    GitLabService gitLabService;
    MergeRequestRepository mergeRequestRepository;
    CommitRepository commitRepository;
    ProjectRepository projectRepository;
    GitManagementUserRepository gitManagementUserRepository;

    @Value("${gitlab.SERVER_URL}")
    String serverUrl;

    @Value("${gitlab.ACCESS_TOKEN}")
    String accessToken;

    public CommitService(GitLabService gitLabService, ProjectRepository projectRepository, CommitRepository commitRepository, MergeRequestRepository mergeRequestRepository, GitManagementUserRepository gitManagementUserRepository) {
        this.gitLabService = gitLabService;
        this.projectRepository = projectRepository;
        this.mergeRequestRepository = mergeRequestRepository;
        this.commitRepository = commitRepository;
        this.gitManagementUserRepository = gitManagementUserRepository;
    }

    public String splitEmail(String email) {
        String[] stringArr = email.split("@");
        return stringArr[0];
    }


    public void saveCommitInfo(Long gitLabProjectId, ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
        Project project = projectRepository.findByGitlabProjectIdAndServerUrl(gitLabProjectId, serverUrl);

        //Save commits associated with each merge request
        List<MergeRequest> mergeRequestList = mergeRequestRepository.findAllByProjectId(project.getId());
        List<String> mrCommitShas = new ArrayList<>(); //Used to filter for the case of orphan commits
        for(MergeRequest mergeRequest : mergeRequestList){
            var gitLabCommits = gitLabService.getMergeRequestCommits(gitLabProjectId, mergeRequest.getIid());
            saveCommitHelper(project, mergeRequest, gitLabCommits, mrCommitShas);
        }

        //Save orphan commits
        var gitLabCommits = gitLabService.getCommits(gitLabProjectId, startDateTime, endDateTime)
                                                           .filter(gitLabCommit -> !mrCommitShas.contains(gitLabCommit.getSha()));
        saveCommitHelper(project, null, gitLabCommits, mrCommitShas);
    }

    private void saveCommitHelper(Project project, MergeRequest mergeRequest,Flux<GitLabCommit> gitLabCommits, List<String> mrCommitShas){
        var gitLabCommitList = gitLabCommits.collectList().block();

        if (gitLabCommitList != null && !gitLabCommitList.isEmpty()) {
            gitLabCommitList.forEach(gitLabCommit -> {
                        Commit commit = commitRepository.findByCommitShaAndProjectId(gitLabCommit.getSha(),project.getId());
                        if(commit != null){return;}

                        String username = splitEmail(gitLabCommit.getAuthorEmail());
                        GitManagementUser gitManagementUser = gitManagementUserRepository.findByUserNameAndServerUrl(username, serverUrl);

                        if(gitManagementUser == null){
                            gitManagementUser = gitManagementUserRepository.findByUsernameAndProjectId(gitLabCommit.getAuthorName(),project.getId());
                            if(gitManagementUser == null){
                                return;
                            }
                        }

                        commit = new Commit(
                                gitLabCommit.getSha(),
                                gitLabCommit.getTitle(),
                                gitLabCommit.getAuthorName(),
                                gitLabCommit.getAuthorEmail(),
                                gitLabCommit.getAuthorName(),
                                gitLabCommit.getAuthorEmail(),
                                gitLabCommit.getCreatedAt(),
                                gitLabCommit.getWebUrl(),
                                project,
                                gitManagementUser
                        );

                        if(mergeRequest != null){
                            mergeRequest.addCommit(commit);
                            mrCommitShas.add(gitLabCommit.getSha());
                        }

                        commitRepository.save(commit);
                    }
            );
        }
    }

}