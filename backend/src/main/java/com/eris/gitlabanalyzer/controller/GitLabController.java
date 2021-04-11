package com.eris.gitlabanalyzer.controller;

import com.eris.gitlabanalyzer.model.gitlabresponse.*;

import com.eris.gitlabanalyzer.service.GitLabService;
import com.eris.gitlabanalyzer.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/gitlab")
public class GitLabController {

    private final ProjectService projectService;
    private final GitLabService requestScopeGitLabService;

    @Autowired
    public GitLabController(ProjectService projectService, GitLabService requestScopeGitLabService) {
        this.projectService = projectService;
        this.requestScopeGitLabService = requestScopeGitLabService;
    }

    @GetMapping(path ="{serverId}/projects")
    public List<GitLabProject> getProjects(@PathVariable("serverId") Long id) {
        // The serverId passed into request is needed for injected GitLabService to be
        // instantiated with correct server info
        return requestScopeGitLabService.getProjects().collectList().block();
    }

    @GetMapping(path ="/projects/{projectId}")
    public Mono<GitLabProject> getProject(@PathVariable("projectId") Long projectId) {
        var project = projectService.getProjectById(projectId);
        return requestScopeGitLabService.getProject(project.getGitLabProjectId());
    }

    @GetMapping(path ="/projects/{projectId}/commit/{sha}/diff")
    public Flux<GitLabFileChange> getCommitDiff(
            @PathVariable("projectId") Long projectId,
            @PathVariable("sha") String sha) {

        var project = projectService.getProjectById(projectId);
        return requestScopeGitLabService.getCommitDiff(project.getGitLabProjectId(), sha);
    }

    @GetMapping(path ="/projects/{projectId}/merge_request/{merge_request_iid}/diff")
    public Flux<GitLabFileChange> getMergeDiff(
            @PathVariable("projectId") Long projectId,
            @PathVariable("merge_request_iid") Long merge_request_iid) {

        var project = projectService.getProjectById(projectId);
        return requestScopeGitLabService.getMergeRequestDiff(project.getGitLabProjectId(), merge_request_iid);
    }

    @GetMapping(path ="/projects/{projectId}/members")
    public Flux<GitLabMember> getMembers(
            @PathVariable("projectId") Long projectId) {

        var project = projectService.getProjectById(projectId);
        return requestScopeGitLabService.getMembers(project.getGitLabProjectId());
    }
}
