package com.eris.gitlabanalyzer.controller;

import com.eris.gitlabanalyzer.model.gitlabresponse.*;

import com.eris.gitlabanalyzer.repository.UserRepository;
import com.eris.gitlabanalyzer.service.GitLabService;
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

    private final UserServerService userServerService;
    private final UserRepository userRepository;

    // TODO Remove after server info is correctly retrieved based on internal projectId
    @Value("${gitlab.SERVER_URL}")
    String serverUrl;

    // TODO Remove after server info is correctly retrieved based on internal projectId
    @Value("${gitlab.ACCESS_TOKEN}")
    String accessToken;

    @Autowired
    public GitLabController(UserServerService userServerService, UserRepository userRepository) {
        this.userServerService = userServerService;
        this.userRepository = userRepository;
    }

    @GetMapping(path ="{serverId}/projects")
    public Flux<GitLabProject> getProjects(Principal principal, @PathVariable("serverId") Long id) {
        //TODO get logged in user based on SSO session params
        var username = principal.getName();
        var user = this.userRepository.findUserByUsername(username);

        var userServer = userServerService.getUserServer(user.get(), id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find server."));
        var gitLabService = new GitLabService(userServer.getServer().getServerUrl(), userServer.getAccessToken());
        return gitLabService.getProjects();
    }

    // Used in notes page for now
    @GetMapping(path ="/projects/{projectId}")
    public Mono<GitLabProject> getProject(
            @PathVariable("projectId") Long projectId
    ) {
        // TODO this endpoint needs to be removed or use an internal projectId to find the correct server that user owns
        var gitLabService = new GitLabService(serverUrl, accessToken);
        return gitLabService.getProject(projectId);
    }

    // TODO: currently there is no direct use for this endpoint, to be removed
    @GetMapping(path ="/projects/{projectId}/merge_requests")
    public Flux<GitLabMergeRequest> getMergeRequests(
            @PathVariable("projectId") Long projectId,
            @RequestParam("startDateTime")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDateTime,
            @RequestParam("endDateTime")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDateTime) {

        // TODO this endpoint needs to be removed or use an internal projectId to find the correct server that user owns
        var gitLabService = new GitLabService(serverUrl, accessToken);
        return gitLabService.getMergeRequests(projectId, startDateTime, endDateTime);
    }

    // TODO: currently there is no direct use for this endpoint, to be removed
    @GetMapping(path ="/projects/{projectId}/merge_request/{merge_request_iid}/commits")
    public Flux<GitLabCommit> getMergeRequestCommits(
            @PathVariable("projectId") Long projectId,
            @PathVariable("merge_request_iid") Long merge_request_iid)  {

        // TODO this endpoint needs to be removed or use an internal projectId to find the correct server that user owns
        var gitLabService = new GitLabService(serverUrl, accessToken);
        return gitLabService.getMergeRequestCommits(projectId, merge_request_iid);
    }

    // TODO: currently there is no direct use for this endpoint, to be removed
    @GetMapping(path ="/projects/{projectId}/commits")
    public Flux<GitLabCommit> getCommits(
            @PathVariable("projectId") Long projectId,
            @RequestParam("startDateTime")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDateTime,
            @RequestParam("endDateTime")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDateTime) {

        // TODO this endpoint needs to be removed or use an internal projectId to find the correct server that user owns
        var gitLabService = new GitLabService(serverUrl, accessToken);
        return gitLabService.getCommits(projectId, startDateTime, endDateTime);
    }

    // TODO: currently there is no direct use for this endpoint, to be removed
    @GetMapping(path ="/projects/{projectId}/commit/{sha}/diff")
    public Flux<GitLabFileChange> getCommitDiff(
            @PathVariable("projectId") Long projectId,
            @PathVariable("sha") String sha) {

        // TODO this endpoint needs to be removed or use an internal projectId to find the correct server that user owns
        var gitLabService = new GitLabService(serverUrl, accessToken);
        return gitLabService.getCommitDiff(projectId, sha);
    }

    // TODO: currently there is no direct use for this endpoint, to be removed
    @GetMapping(path ="/projects/{projectId}/merge_request/{merge_request_iid}/diff")
    public Flux<GitLabFileChange> getMergeDiff(
            @PathVariable("projectId") Long projectId,
            @PathVariable("merge_request_iid") Long merge_request_iid) {

        // TODO this endpoint needs to be removed or use an internal projectId to find the correct server that user owns
        var gitLabService = new GitLabService(serverUrl, accessToken);
        return gitLabService.getMergeRequestDiff(projectId, merge_request_iid);
    }

    // Used in notes page for now
    @GetMapping(path = "/projects/{projectId}/merge_requests/{merge_request_iid}/notes")
    public Flux<GitLabMergeRequestNote> getMergeRequestNotes(
            @PathVariable("projectId") Long projectId,
            @PathVariable("merge_request_iid") Long merge_request_iid) {

        // TODO this endpoint needs to be removed or use an internal projectId to find the correct server that user owns
        var gitLabService = new GitLabService(serverUrl, accessToken);
        return gitLabService.getMergeRequestNotes(projectId, merge_request_iid);
    }

    // Used in notes page for now
    @GetMapping(path = "/projects/{projectId}/issues")
    public Flux<GitLabIssue> getIssues(
            @PathVariable("projectId") Long projectId,
            @RequestParam("startDateTime")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDateTime,
            @RequestParam("endDateTime")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDateTime) {

        // TODO this endpoint needs to be removed or use an internal projectId to find the correct server that user owns
        var gitLabService = new GitLabService(serverUrl, accessToken);
        return gitLabService.getIssues(projectId, startDateTime, endDateTime);
    }

    // Used in notes page for now
    @GetMapping(path = "/projects/{projectId}/issues/{issue_iid}/notes")
    public Flux<GitLabIssueNote> getIssueNotes(
            @PathVariable("projectId") Long projectId,
            @PathVariable("issue_iid") Long issue_iid) {

        // TODO this endpoint needs to be removed or use an internal projectId to find the correct server that user owns
        var gitLabService = new GitLabService(serverUrl, accessToken);
        return gitLabService.getIssueNotes(projectId, issue_iid);
    }
    @GetMapping(path ="/projects/{projectId}/members")
    public Flux<GitLabMember> getMembers(
            @PathVariable("projectId") Long projectId) {

        // TODO this endpoint needs to be removed or use an internal projectId to find the correct server that user owns
        var gitLabService = new GitLabService(serverUrl, accessToken);
        return gitLabService.getMembers(projectId);
    }
}
