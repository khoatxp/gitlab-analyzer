package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.*;
import com.eris.gitlabanalyzer.viewmodel.CommitAuthorRequestBody;
import com.eris.gitlabanalyzer.viewmodel.CommitAuthorView;
import com.eris.gitlabanalyzer.model.gitlabresponse.GitLabCommit;
import com.eris.gitlabanalyzer.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.ZoneOffset;
import java.util.*;
import java.time.OffsetDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CommitService {

    MergeRequestRepository mergeRequestRepository;
    CommitRepository commitRepository;
    ProjectRepository projectRepository;
    GitManagementUserRepository gitManagementUserRepository;
    CommitCommentRepository commitCommentRepository;
    ScoreService scoreService;
    CommitAuthorRepository commitAuthorRepository;

    // TODO Remove after server info is correctly retrieved based on internal projectId
    @Value("${gitlab.SERVER_URL}")
    String serverUrl;

    // TODO Remove after server info is correctly retrieved based on internal projectId
    @Value("${gitlab.ACCESS_TOKEN}")
    String accessToken;

    public CommitService(MergeRequestRepository mergeRequestRepository, CommitRepository commitRepository, ProjectRepository projectRepository, GitManagementUserRepository gitManagementUserRepository, CommitCommentRepository commitCommentRepository, ScoreService scoreService, CommitAuthorRepository commitAuthorRepository ) {
        this.mergeRequestRepository = mergeRequestRepository;
        this.commitRepository = commitRepository;
        this.projectRepository = projectRepository;
        this.gitManagementUserRepository = gitManagementUserRepository;
        this.commitCommentRepository = commitCommentRepository;
        this.commitAuthorRepository = commitAuthorRepository;
        this.scoreService = scoreService;
    }

    public String splitEmail(String email) {
        String[] stringArr = email.split("@");
        return stringArr[0];
    }


    public void saveCommitInfo(Project project, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        //Save commits associated with each merge request
        List<MergeRequest> mergeRequestList = mergeRequestRepository.findAllByProjectId(project.getId());
        List<String> mrCommitShas = new ArrayList<>(); //Used to filter for the case of orphan commits

        // TODO use an internal projectId to find the correct server
        var gitLabService = new GitLabService(serverUrl, accessToken);

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

                    saveCommitAuthor(project,gitLabCommit);

                    commit = new Commit(
                            gitLabCommit.getSha(),
                            gitLabCommit.getTitle(),
                            gitLabCommit.getAuthorName(),
                            gitLabCommit.getAuthorEmail(),
                            gitLabCommit.getCreatedAt().withOffsetSameInstant(ZoneOffset.UTC),
                            gitLabCommit.getWebUrl(),
                            project
                    );

                    if(mergeRequest != null){
                        mergeRequest.addCommit(commit);
                        mrCommitShas.add(gitLabCommit.getSha());
                    }


                    commit = commitRepository.save(commit);
                    saveCommitComment(project, commit);
                    scoreService.saveCommitDiffMetrics(commit);
                }
        );

        setAllSharedMergeRequests(project.getId());
    }

    public CommitAuthor saveCommitAuthor(Project project, GitLabCommit gitLabCommit){
        Optional <CommitAuthor> existingAuthor = commitAuthorRepository.findByAuthorNameAndAuthorEmailAndProjectId(
                gitLabCommit.getAuthorName(),
                gitLabCommit.getAuthorEmail(),
                project.getId()
        );

        if(existingAuthor.isPresent()){
            return existingAuthor.get();
        }

        CommitAuthor commitAuthor = new CommitAuthor(gitLabCommit.getAuthorName(), gitLabCommit.getAuthorEmail(), project);

        //First attempt using author username extracted from email
        String username = splitEmail(gitLabCommit.getAuthorEmail());
        GitManagementUser gitManagementUser = gitManagementUserRepository.findByUsernameAndServerUrl(username, serverUrl);

        if(gitManagementUser == null){
            //Second attempt using author name
            gitManagementUser = gitManagementUserRepository.findByUsernameAndServerUrl(gitLabCommit.getAuthorName(),serverUrl);
        }

        if(gitManagementUser != null){
            commitAuthor.setGitManagementUser(gitManagementUser);
        }

        return commitAuthorRepository.save(commitAuthor);
    }

    public void saveCommitComment(Project project, Commit commit){
        // TODO use an internal projectId to find the correct server
        var gitLabService = new GitLabService(serverUrl, accessToken);
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

    public List<CommitAuthorView> getCommitAuthors(Long projectId){
        List<CommitAuthorView> mappedCommitAuthors = commitAuthorRepository.findByProjectId(projectId);
        List<CommitAuthorView> unmappedCommitAuthors = commitAuthorRepository.findUnmappedCommitAuthorsByProjectId(projectId);
        return Stream.of(mappedCommitAuthors, unmappedCommitAuthors).flatMap(Collection::stream)
                                                                    .sorted(Comparator.comparing(CommitAuthorView::getAuthorName))
                                                                    .collect(Collectors.toList());
    }

    public List<CommitAuthorView> getUnmappedCommitAuthors(Long projectId){
        return commitAuthorRepository.findUnmappedCommitAuthorsByProjectId(projectId);
    }

    public void mapNewCommitAuthors(Long projectId, List<CommitAuthorRequestBody> commitAuthors){
        commitAuthors.forEach(commitAuthor -> {
            if(commitAuthor.getMappedGitManagementUserId() == null){
                return;
            }

            commitAuthorRepository.updateCommitAuthors(
                    commitAuthor.getMappedGitManagementUserId(),
                    commitAuthor.getAuthorName(),
                    commitAuthor.getAuthorEmail(),
                    projectId);
        });
    }

    public List<Commit> getCommits(Long projectId){
        return commitRepository.findAllByProjectId(projectId);
    }

    public List<Commit> getCommitsOfGitManagementUser(Long projectId, Long gitManagementUserId){
        return commitRepository.findByProjectIdAndGitManagementUserId(projectId, gitManagementUserId);
    }

    public List<Commit> getCommitsInDateRange(Long projectId, OffsetDateTime startDateTime, OffsetDateTime endDateTime){
        List<Commit> commits = commitRepository.findAllByProjectIdAndDateRange(projectId, startDateTime, endDateTime);

        return commits;
    }

    public List<Commit> getCommitsOfGitManagementUserInDateRange(Long projectId, Long gitManagementUserId, OffsetDateTime startDateTime, OffsetDateTime endDateTime){
        List<Commit> commits = commitRepository.findAllByProjectIdAndDateRangeAndGitManagementUserId(projectId, gitManagementUserId, startDateTime, endDateTime);
        return commits;
    }



    public void setAllSharedMergeRequests(Long projectId){
        List<MergeRequest> mergeRequests = mergeRequestRepository.findAllByProjectId(projectId);
        for(MergeRequest mr : mergeRequests){
            // reset mr sharedStatus so if was true and new mapping sets to false, won't remain true
            Set<Long> sharedWith = new LinkedHashSet<>();
            List<Commit> commits = commitRepository.findCommitByMergeRequest_Id(mr.getId());
            for(Commit commit : commits){
               Optional <CommitAuthor> commitAuthor = commitAuthorRepository.findByAuthorNameAndAuthorEmailAndProjectId(commit.getAuthorName(),
                        commit.getAuthorEmail(), projectId);

               commitAuthor.ifPresent(author -> {
                   GitManagementUser gitManagementUser = author.getGitManagementUser();
                   if(isMergeRequestShared(mr, gitManagementUser)){
                       sharedWith.add(gitManagementUser.getId());
                   }
               });
            }
            mr.setSharedWith(sharedWith);
            mergeRequestRepository.save(mr);
        }
    }
    // checks whether supplied gitManagementUser is the same as one stored in MR
    private boolean isMergeRequestShared(MergeRequest mr, GitManagementUser gitManagementUser){
        if(gitManagementUser != null){
            if(!mr.getGitManagementUser().getId().equals(gitManagementUser.getId())){
                return true;
            }
        }
        return false;
    }

}