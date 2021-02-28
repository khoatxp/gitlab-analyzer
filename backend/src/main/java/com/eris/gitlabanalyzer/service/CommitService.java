package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.*;
import com.eris.gitlabanalyzer.model.gitlabresponse.GitLabCommit;
import com.eris.gitlabanalyzer.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;


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

                        //First attempt to map member and commit using email
                        String username = splitEmail(gitLabCommit.getAuthorEmail());
                        GitManagementUser gitManagementUser = gitManagementUserRepository.findByUserNameAndServerUrl(username, serverUrl);

                        if(gitManagementUser == null){
                            //Second attempt using author name
                            gitManagementUser = gitManagementUserRepository.findByUserNameAndServerUrl(gitLabCommit.getAuthorName(),serverUrl);
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
                        saveCommitComment(project, gitLabCommit.getSha());
                    }
            );
        }
    }

    public void saveCommitComment(Project project, String commitSha){
        Commit commit = commitRepository.findByCommitShaAndProjectId(commitSha, project.getId());
        var gitLabCommitComments = gitLabService.getCommitComments(project.getGitLabProjectId(), commitSha);
        var gitLabCommitCommentList = gitLabCommitComments.collectList().block();
        if (gitLabCommitCommentList != null && !gitLabCommitCommentList.isEmpty()) {
            gitLabCommitCommentList.forEach(gitLabCommitComment -> {
                GitManagementUser gitManagementUser = gitManagementUserRepository.findByUserNameAndServerUrl(gitLabCommitComment.getAuthor().getUsername(),serverUrl);
                if(gitManagementUser == null){
                    return;
                }
                CommitComment commitComment = commitCommentRepository.findByUsernameAndCreatedAtAndCommitSha(gitLabCommitComment.getAuthor().getUsername(),gitLabCommitComment.getCreatedAt(),commitSha);
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

}