package com.eris.gitlabanalyzer.controller;

import com.eris.gitlabanalyzer.model.gitlabresponse.*;

import com.eris.gitlabanalyzer.service.AuthService;
import com.eris.gitlabanalyzer.service.GitLabService;
import com.eris.gitlabanalyzer.service.ProjectService;
import com.eris.gitlabanalyzer.service.UserServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.time.OffsetDateTime;

@RestController
@RequestMapping(path = "/api/v1/gitlab")
public class GitLabController {

    private final AuthService authService;
    private final UserServerService userServerService;
    private final ProjectService projectService;

    // TODO Remove after server info is correctly retrieved based on internal projectId
    @Value("${gitlab.SERVER_URL}")
    String serverUrl;

    // TODO Remove after server info is correctly retrieved based on internal projectId
    @Value("${gitlab.ACCESS_TOKEN}")
    String accessToken;

    @Autowired
    public GitLabController(AuthService authService, UserServerService userServerService, ProjectService projectService) {
        this.authService = authService;
        this.userServerService = userServerService;
        this.projectService = projectService;
    }

    @GetMapping(path ="{serverId}/projects")
    public Flux<GitLabProject> getProjects(Principal principal, @PathVariable("serverId") Long id) {
        var user = authService.getLoggedInUser(principal);
        var userServer = userServerService.getUserServer(user, id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find server."));
        var gitLabService = new GitLabService(userServer.getServer().getServerUrl(), userServer.getAccessToken());
        return gitLabService.getProjects();
    }

    // Used in notes page for now
    @GetMapping(path ="/projects/{projectId}")
    public Mono<GitLabProject> getProject(@PathVariable("projectId") Long projectId) {
        // TODO validate that the user has permissions for the server
        var project = projectService.getProjectById(projectId);
        var gitLabService = new GitLabService(serverUrl, accessToken);
        return gitLabService.getProject(project.getGitLabProjectId());
    }

    // TODO: currently there is no direct use for this endpoint, to be removed
    @GetMapping(path ="/projects/{projectId}/merge_requests")
    public Flux<GitLabMergeRequest> getMergeRequests(
            @PathVariable("projectId") Long projectId,
            @RequestParam("startDateTime")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDateTime,
            @RequestParam("endDateTime")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDateTime) {

        // TODO validate that the user has permissions for the server
        var project = projectService.getProjectById(projectId);
        var gitLabService = new GitLabService(serverUrl, accessToken);
        return gitLabService.getMergeRequests(project.getGitLabProjectId(), startDateTime, endDateTime);
    }

    // TODO: currently there is no direct use for this endpoint, to be removed
    @GetMapping(path ="/projects/{projectId}/merge_request/{merge_request_iid}/commits")
    public Flux<GitLabCommit> getMergeRequestCommits(
            @PathVariable("projectId") Long projectId,
            @PathVariable("merge_request_iid") Long merge_request_iid)  {

        // TODO validate that the user has permissions for the server
        var project = projectService.getProjectById(projectId);
        var gitLabService = new GitLabService(serverUrl, accessToken);
        return gitLabService.getMergeRequestCommits(project.getGitLabProjectId(), merge_request_iid);
    }

    // TODO: currently there is no direct use for this endpoint, to be removed
    @GetMapping(path ="/projects/{projectId}/commits")
    public Flux<GitLabCommit> getCommits(
            @PathVariable("projectId") Long projectId,
            @RequestParam("startDateTime")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDateTime,
            @RequestParam("endDateTime")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDateTime) {

        // TODO validate that the user has permissions for the server
        var project = projectService.getProjectById(projectId);
        var gitLabService = new GitLabService(serverUrl, accessToken);
        return gitLabService.getCommits(project.getGitLabProjectId(), startDateTime, endDateTime);
    }

    @GetMapping(path ="/projects/{projectId}/commit/{sha}/diff")
    public Flux<GitLabFileChange> getCommitDiff(
            @PathVariable("projectId") Long projectId,
            @PathVariable("sha") String sha) {

        // TODO validate that the user has permissions for the server
        var project = projectService.getProjectById(projectId);
        var gitLabService = new GitLabService(serverUrl, accessToken);
        return gitLabService.getCommitDiff(project.getGitLabProjectId(), sha);
    }

    @GetMapping(path ="/projects/{projectId}/merge_request/{merge_request_iid}/diff")
    public Flux<GitLabFileChange> getMergeDiff(
            @PathVariable("projectId") Long projectId,
            @PathVariable("merge_request_iid") Long merge_request_iid) {

        // TODO validate that the user has permissions for the server
        var project = projectService.getProjectById(projectId);
        var gitLabService = new GitLabService(serverUrl, accessToken);
        return gitLabService.getMergeRequestDiff(project.getGitLabProjectId(), merge_request_iid);
    }

    // Used in notes page for now
    @GetMapping(path = "/projects/{projectId}/merge_requests/{merge_request_iid}/notes")
    public Flux<GitLabMergeRequestNote> getMergeRequestNotes(
            @PathVariable("projectId") Long projectId,
            @PathVariable("merge_request_iid") Long merge_request_iid) {

        // TODO validate that the user has permissions for the server
        var project = projectService.getProjectById(projectId);
        var gitLabService = new GitLabService(serverUrl, accessToken);
        return gitLabService.getMergeRequestNotes(project.getGitLabProjectId(), merge_request_iid);
    }

    // Used in notes page for now
    @GetMapping(path = "/projects/{projectId}/issues")
    public Flux<GitLabIssue> getIssues(
            @PathVariable("projectId") Long projectId,
            @RequestParam("startDateTime")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDateTime,
            @RequestParam("endDateTime")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDateTime) {

        // TODO validate that the user has permissions for the server
        var project = projectService.getProjectById(projectId);
        var gitLabService = new GitLabService(serverUrl, accessToken);
        return gitLabService.getIssues(project.getGitLabProjectId(), startDateTime, endDateTime);
    }

    // Used in notes page for now
    @GetMapping(path = "/projects/{projectId}/issues/{issue_iid}/notes")
    public Flux<GitLabIssueNote> getIssueNotes(
            @PathVariable("projectId") Long projectId,
            @PathVariable("issue_iid") Long issue_iid) {

        // TODO validate that the user has permissions for the server
        var project = projectService.getProjectById(projectId);
        var gitLabService = new GitLabService(serverUrl, accessToken);
        return gitLabService.getIssueNotes(project.getGitLabProjectId(), issue_iid);
    }
    @GetMapping(path ="/projects/{projectId}/members")
    public Flux<GitLabMember> getMembers(
            @PathVariable("projectId") Long projectId) {

        // TODO validate that the user has permissions for the server
        var project = projectService.getProjectById(projectId);
        var gitLabService = new GitLabService(serverUrl, accessToken);
        return gitLabService.getMembers(project.getGitLabProjectId());
    }
}
