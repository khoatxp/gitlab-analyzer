package com.eris.gitlabanalyzer.controller;

import com.eris.gitlabanalyzer.model.GitManagementUser;
import com.eris.gitlabanalyzer.service.GitManagementUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GitManagementUserController {
    private final GitManagementUserService gitManagementUserService;
    @Autowired
    public GitManagementUserController(GitManagementUserService gitManagementUserService) {
        this.gitManagementUserService = gitManagementUserService;
    }

    @GetMapping("/api/v1/managementuser/members")
    public List<GitManagementUser> getMembersByProjectId(@RequestParam(required = true) String projectId){
        return gitManagementUserService.getMembersByProjectId(Long.parseLong(projectId));
    }
}
