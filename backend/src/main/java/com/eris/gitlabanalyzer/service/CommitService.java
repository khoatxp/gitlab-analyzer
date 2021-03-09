package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.*;
import com.eris.gitlabanalyzer.model.gitlabresponse.GitLabCommit;
import com.eris.gitlabanalyzer.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import java.util.ArrayList;
import java.util.List;
import java.time.OffsetDateTime;

@Service
public class CommitService {

    GitLabService gitLabService;
    MergeRequestRepository mergeRequestRepository;
    CommitRepository commitRepository;
    ProjectRepository projectRepository;
    GitManagementUserRepository gitManagementUserRepository;
    CommitCommentRepository commitCommentRepository;

    @Value("${gitlab.SERVER_URL}")
    String serverUrl;

    @Value("${gitlab.ACCESS_TOKEN}")
    String accessToken;

    public CommitService(GitLabService gitLabService, MergeRequestRepository mergeRequestRepository, CommitRepository commitRepository, ProjectRepository projectRepository, GitManagementUserRepository gitManagementUserRepository, CommitCommentRepository commitCommentRepository) {
        this.gitLabService = gitLabService;
        this.mergeRequestRepository = mergeRequestRepository;
        this.commitRepository = commitRepository;
        this.projectRepository = projectRepository;
        this.gitManagementUserRepository = gitManagementUserRepository;
        this.commitCommentRepository = commitCommentRepository;
    }

    public String splitEmail(String email) {
        String[] stringArr = email.split("@");
        return stringArr[0];
    }


    public void saveCommitInfo(Project project, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        //Save commits associated with each merge request
        List<MergeRequest> mergeRequestList = mergeRequestRepository.findAllByProjectId(project.getId());
        List<String> mrCommitShas = new ArrayList<>(); //Used to filter for the case of orphan commits

        mergeRequestList.forEach(mergeRequest -> {
            var gitLabCommits = gitLabService.getMergeRequestCommits(project.getGitLabProjectId(), mergeRequest.getIid());
            saveCommitHelper(project, mergeRequest, gitLabCommits, mrCommitShas);
        });

        //Save orphan commits
        var gitLabCommits = gitLabService.getCommits(project.getGitLabProjectId(), startDateTime, endDateTime)
                                                           .filter(gitLabCommit -> !mrCommitShas.contains(gitLabCommit.getSha()));
        saveCommitHelper(project, null, gitLabCommits, mrCommitShas);
    }

    public void saveCommitHelper(Project project, MergeRequest mergeRequest,Flux<GitLabCommit> gitLabCommits, List<String> mrCommitShas){
        var gitLabCommitList = gitLabCommits.collectList().block();

        gitLabCommitList.forEach(gitLabCommit -> {
                    Commit commit = commitRepository.findByCommitShaAndProjectId(gitLabCommit.getSha(),project.getId());
                    if(commit != null){return;}

                    //TODO check for the commit mapping table if the author is mapped and map that author to the associated git management user
                    //First attempt to map member and commit using email
                    String username = splitEmail(gitLabCommit.getAuthorEmail());
                    GitManagementUser gitManagementUser = gitManagementUserRepository.findByUsernameAndServerUrl(username, serverUrl);

                    if(gitManagementUser == null){
                        //Second attempt using author name
                        gitManagementUser = gitManagementUserRepository.findByUsernameAndServerUrl(gitLabCommit.getAuthorName(),serverUrl);
                    }

                    commit = new Commit(
                            gitLabCommit.getSha(),
                            gitLabCommit.getTitle(),
                            gitLabCommit.getAuthorName(),
                            gitLabCommit.getAuthorEmail(),
                            gitLabCommit.getCreatedAt(),
                            gitLabCommit.getWebUrl(),
                            project
                    );

                    if(mergeRequest != null){
                        mergeRequest.addCommit(commit);
                        mrCommitShas.add(gitLabCommit.getSha());
                    }

                    if(gitManagementUser!=null){
                        commit.setGitManagementUser(gitManagementUser);
                    }

                    commit = commitRepository.save(commit);
                    saveCommitComment(project, commit);
                }
        );

    }

    //TODO we might not need to store commit comments
    public void saveCommitComment(Project project, Commit commit){
        var gitLabCommitComments = gitLabService.getCommitComments(project.getGitLabProjectId(), commit.getSha());
        var gitLabCommitCommentList = gitLabCommitComments.collectList().block();

        gitLabCommitCommentList.parallelStream().forEach(gitLabCommitComment -> {
            GitManagementUser gitManagementUser = gitManagementUserRepository.findByGitLabUserIdAndServerUrl(gitLabCommitComment.getAuthor().getId(),serverUrl);
            if(gitManagementUser == null){
                return;
            }
            CommitComment commitComment = commitCommentRepository.findByGitLabUserIdAndCreatedAtAndCommitSha(gitLabCommitComment.getAuthor().getId(),gitLabCommitComment.getCreatedAt(),commit.getSha());
            if(commitComment == null){
                commitComment = new CommitComment(
                        gitManagementUser,
                        commit,
                        gitLabCommitComment.getNote(),
                        gitLabCommitComment.getCreatedAt());
            }
            commitCommentRepository.save(commitComment);
        });
    }

}