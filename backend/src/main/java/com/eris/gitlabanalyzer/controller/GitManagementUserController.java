package com.eris.gitlabanalyzer.controller;

import com.eris.gitlabanalyzer.model.frontendrequest.CommitAuthorRequest;
import com.eris.gitlabanalyzer.model.frontendresponse.CommitAuthorResponse;
import com.eris.gitlabanalyzer.model.frontendresponse.GitManagementUserResponse;
import com.eris.gitlabanalyzer.service.CommitService;
import com.eris.gitlabanalyzer.service.GitManagementUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GitManagementUserController {
    private final GitManagementUserService gitManagementUserService;
    private final CommitService commitService;

    @Autowired
    public GitManagementUserController(GitManagementUserService gitManagementUserService, CommitService commitService) {
        this.gitManagementUserService = gitManagementUserService;
        this.commitService = commitService;
    }

    @GetMapping("/api/v1/{projectId}/managementuser/members")
    public List<GitManagementUserResponse> getMembersByProjectId(@PathVariable("projectId") Long projectId){
        return gitManagementUserService.getMembersByProjectId(projectId);
    }
    @GetMapping("/api/v1/{projectId}/commitauthor")
    public List<CommitAuthorResponse> getCommitAuthorsByProjectId(@PathVariable("projectId") Long projectId){
        return commitService.getCommitAuthorsByProjectId(projectId);
    }
    @PostMapping("api/v1/{projectId}/managementuser/mapping")
    public void mapNewCommitAuthorsToCommits(
            @PathVariable("projectId") Long projectId,
            @RequestBody List<CommitAuthorRequest> commitAuthors) {
        commitService.mapNewCommitAuthorsToCommits(projectId,commitAuthors);
    }
}
