package com.eris.gitlabanalyzer.controller;

import com.eris.gitlabanalyzer.viewmodel.CommitAuthorRequestBody;
import com.eris.gitlabanalyzer.viewmodel.CommitAuthorView;
import com.eris.gitlabanalyzer.viewmodel.GitManagementUserView;
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
    public List<GitManagementUserView> getMembersByProjectId(@PathVariable("projectId") Long projectId){
        return gitManagementUserService.getMembersByProjectId(projectId);
    }
    @GetMapping("/api/v1/{projectId}/commitauthor")
    public List<CommitAuthorView> getCommitAuthorsByProjectId(@PathVariable("projectId") Long projectId){
        return commitService.getCommitAuthorsByProjectId(projectId);
    }
    @PostMapping("api/v1/{projectId}/managementuser/mapping")
    public void mapNewCommitAuthorsToCommits(
            @PathVariable("projectId") Long projectId,
            @RequestBody List<CommitAuthorRequestBody> commitAuthors) {
        commitService.mapNewCommitAuthorsToCommits(projectId,commitAuthors);
    }
}
