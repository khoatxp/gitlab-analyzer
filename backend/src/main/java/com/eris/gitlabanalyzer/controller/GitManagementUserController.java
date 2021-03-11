package com.eris.gitlabanalyzer.controller;

import com.eris.gitlabanalyzer.viewmodel.GitManagementUserView;
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

    @GetMapping("/api/v1/{projectId}/managementusers/members")
    public List<GitManagementUserView> getMembers(@PathVariable("projectId") Long projectId){
        return gitManagementUserService.getMembers(projectId);
    }
}
