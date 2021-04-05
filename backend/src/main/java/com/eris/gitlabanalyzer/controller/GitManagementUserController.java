package com.eris.gitlabanalyzer.controller;

import com.eris.gitlabanalyzer.model.GitManagementUser;
import com.eris.gitlabanalyzer.viewmodel.GitManagementUserView;
import com.eris.gitlabanalyzer.service.GitManagementUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@RestController
public class GitManagementUserController {
    private final GitManagementUserService gitManagementUserService;

    @Autowired
    public GitManagementUserController(GitManagementUserService gitManagementUserService) {
        this.gitManagementUserService = gitManagementUserService;
    }

    @GetMapping("/api/v1/{projectId}/managementusers/members")
    public Stream<GitManagementUserView> getMembers(@PathVariable("projectId") Long projectId){
        return gitManagementUserService.getMembers(projectId).stream()
                .sorted(Comparator.comparing(GitManagementUser::getUsername))
                .map(GitManagementUserView::fromGitManagementUser);
    }

    @GetMapping("/api/v1/managementusers/members/{gitManagementUserId}")
    public GitManagementUserView getMember(@PathVariable("gitManagementUserId") Long gitManagementUserId){
        return GitManagementUserView.fromGitManagementUser(gitManagementUserService.getMember(gitManagementUserId));
    }
}
