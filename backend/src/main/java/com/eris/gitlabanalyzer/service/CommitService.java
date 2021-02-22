package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.Commit;
import com.eris.gitlabanalyzer.model.GitManagementUser;
import com.eris.gitlabanalyzer.model.MergeRequest;
import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.repository.CommitRepository;
import com.eris.gitlabanalyzer.repository.GitManagementUserRepository;
import com.eris.gitlabanalyzer.repository.MergeRequestRepository;
import com.eris.gitlabanalyzer.repository.ProjectRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

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
        List<MergeRequest> mergeRequestList = mergeRequestRepository.findAllByProjectId(project.getId());

        for(MergeRequest mergeRequest : mergeRequestList){
            var gitLabCommits = gitLabService.getMergeRequestCommits(gitLabProjectId, mergeRequest.getIid());
            var gitLabCommitList = gitLabCommits.collectList().block();

            if (gitLabCommitList != null && !gitLabCommitList.isEmpty()) {
                gitLabCommitList.forEach(gitLabCommit -> {
                            String username = splitEmail(gitLabCommit.getAuthorEmail());
                            GitManagementUser gitManagementUser = gitManagementUserRepository.findByUserNameAndServerUrl(username, serverUrl);
                            if(gitManagementUser == null){
                                gitManagementUser = gitManagementUserRepository.findByUsernameAndProjectId(gitLabCommit.getAuthorName(),project.getId());
                                if(gitManagementUser == null){
                                    return;
                                }
                            }
                            Commit commit = new Commit(
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
                            mergeRequest.addCommit(commit);
                            commitRepository.save(commit);
                        }
                );
            }
        }

        var gitLabCommits = gitLabService.getCommits(gitLabProjectId, startDateTime, endDateTime);
        var gitLabCommitList = gitLabCommits.collectList().block();
        if (gitLabCommitList != null && !gitLabCommitList.isEmpty()) {
            gitLabCommitList.forEach(gitLabCommit -> {
                        Commit commit = commitRepository.findByCommitSha(gitLabCommit.getSha());
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
                        commitRepository.save(commit);
                    }
            );
        }
    }

}