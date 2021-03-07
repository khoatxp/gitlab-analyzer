package com.eris.gitlabanalyzer.controller;

import com.eris.gitlabanalyzer.model.gitlabresponse.*;
import com.eris.gitlabanalyzer.service.GitLabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;

@RestController
@RequestMapping(path = "/api/v1/gitlab")
public class GitLabController {

    private final GitLabService gitLabService;

    @Autowired
    public GitLabController(GitLabService gitLabService){
        this.gitLabService = gitLabService;
    }

    //TODO assuming in the future we will have a and id (maybe serverId) to lookup
    // the the server url and token info from the database based on user's selected server
    //@GetMapping(path ="{serverId}/projects")
    // public List<Project> getProjects(@PathVariable("serverId") Long id)
    @GetMapping(path ="/projects")
    public Flux<GitLabProject> getProjects() {
        return gitLabService.getProjects();
    }

    // Used in notes page for now
    @GetMapping(path ="/projects/{projectId}")
    public Mono<GitLabProject> getProject(
            @PathVariable("projectId") Long projectId
    ) {
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
        return gitLabService.getMergeRequests(projectId, startDateTime, endDateTime);
    }

    // TODO: currently there is no direct use for this endpoint, to be removed
    @GetMapping(path ="/projects/{projectId}/merge_request/{merge_request_iid}/commits")
    public Flux<GitLabCommit> getMergeRequestCommits(
            @PathVariable("projectId") Long projectId,
            @PathVariable("merge_request_iid") Long merge_request_iid)  {
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
        return gitLabService.getCommits(projectId, startDateTime, endDateTime);
    }

    // TODO: currently there is no direct use for this endpoint, to be removed
    @GetMapping(path ="/projects/{projectId}/commit/{sha}/diff")
    public Flux<GitLabFileChange> getCommitDiff(
            @PathVariable("projectId") Long projectId,
            @PathVariable("sha") String sha) {
        return gitLabService.getCommitDiff(projectId, sha);
    }

    // TODO: currently there is no direct use for this endpoint, to be removed
    @GetMapping(path ="/projects/{projectId}/merge_request/{merge_request_iid}/diff")
    public Flux<GitLabFileChange> getMergeDiff(
            @PathVariable("projectId") Long projectId,
            @PathVariable("merge_request_iid") Long merge_request_iid) {
        return gitLabService.getMergeRequestDiff(projectId, merge_request_iid);
    }

    // Used in notes page for now
    @GetMapping(path = "/projects/{projectId}/merge_requests/{merge_request_iid}/notes")
    public Flux<GitLabMergeRequestNote> getMergeRequestNotes(
            @PathVariable("projectId") Long projectId,
            @PathVariable("merge_request_iid") Long merge_request_iid) {
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
        return gitLabService.getIssues(projectId, startDateTime, endDateTime);
    }

    // Used in notes page for now
    @GetMapping(path = "/projects/{projectId}/issues/{issue_iid}/notes")
    public Flux<GitLabIssueNote> getIssueNotes(
            @PathVariable("projectId") Long projectId,
            @PathVariable("issue_iid") Long issue_iid) {
        return gitLabService.getIssueNotes(projectId, issue_iid);
    }
    @GetMapping(path ="/projects/{projectId}/members")
    public Flux<GitLabMember> getMembers(
            @PathVariable("projectId") Long projectId) {
        return gitLabService.getMembers(projectId);
    }
}
