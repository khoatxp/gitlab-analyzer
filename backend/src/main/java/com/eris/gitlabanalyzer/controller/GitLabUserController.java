package com.eris.gitlabanalyzer.controller;

import com.eris.gitlabanalyzer.model.GitLabUser;
import com.eris.gitlabanalyzer.service.GitLabUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GitLabUserController {
    private final GitLabUserService gitLabUserService;
    @Autowired
    public GitLabUserController(GitLabUserService gitLabUserService) {
        this.gitLabUserService = gitLabUserService;
    }

    @GetMapping("/api/v1/members")
    public List<GitLabUser> getMembersByProjectId(@RequestParam(required = true) String projectId){
        return gitLabUserService.getMembersByProjectId(Long.parseLong(projectId));
    }
}
