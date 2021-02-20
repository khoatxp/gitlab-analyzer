package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.ZonedDateTime;

@Service
public class GitLabService {
    private final WebClient webClient;
    private final String projectPath = "api/v4/projects/";

    @Value("${gitlab.SERVER_URL}")
    String serverUrl;

    @Value("${gitlab.ACCESS_TOKEN}")
    String accessToken;

    public GitLabService() {
        this.webClient = WebClient.create();
    }

    public Flux<GitLabProject> getProjects(){
        URI gitlabUrl = UriComponentsBuilder.fromUriString(serverUrl)
                .path(projectPath)
                .queryParam("per_page", 100)
                .build()
                .encode()
                .toUri();

        WebClient.RequestHeadersSpec<?> headersSpec = webClient.get()
                .uri(gitlabUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        return headersSpec.retrieve().bodyToFlux(GitLabProject.class);
    }

    public Mono<GitLabProject> getProject(Long projectId) {
        URI gitlabUrl = UriComponentsBuilder.fromUriString(serverUrl)
                .path(projectPath + projectId)
                .build()
                .encode()
                .toUri();

        WebClient.RequestHeadersSpec<?> headersSpec = webClient.get()
                .uri(gitlabUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        return headersSpec.retrieve().bodyToMono(GitLabProject.class);
    }

    public Flux<GitLabMember> getMembers(Long projectId) {
        URI gitlabUrl = UriComponentsBuilder.fromUriString(serverUrl)
                .path(projectPath + projectId + "/members")
                .queryParam("per_page", 100)
                .build()
                .encode()
                .toUri();

        WebClient.RequestHeadersSpec<?> headersSpec = webClient.get()
                .uri(gitlabUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        return headersSpec.retrieve().bodyToFlux(GitLabMember.class);
    }

    public Flux<GitLabMergeRequest> getMergeRequests(Long projectId, ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
        URI gitlabUrl = UriComponentsBuilder.fromUriString(serverUrl)
                .path(projectPath + projectId + "/merge_requests")
                .queryParam("state", "merged")
                .queryParam("target_branch", "master")
                .queryParam("created_after", startDateTime.toString())
                .queryParam("updated_before", endDateTime.toString())
                .queryParam("per_page", 100)
                .build()
                .encode()
                .toUri();

        WebClient.RequestHeadersSpec<?> headersSpec = webClient.get()
                .uri(gitlabUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        return headersSpec.retrieve().bodyToFlux(GitLabMergeRequest.class);
    }

    public Flux<GitLabCommit> getMergeRequestCommits(Long projectId, Long mergeRequestIid) {
        URI gitlabUrl = UriComponentsBuilder.fromUriString(serverUrl)
                .path(projectPath + projectId + "/merge_requests/" + mergeRequestIid + "/commits")
                .queryParam("per_page", 100)
                .build()
                .encode()
                .toUri();

        WebClient.RequestHeadersSpec<?> headersSpec = webClient.get()
                .uri(gitlabUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        return headersSpec.retrieve().bodyToFlux(GitLabCommit.class);
    }

    public Flux<GitLabCommit> getCommits(Long projectId, ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
        URI gitlabUrl = UriComponentsBuilder.fromUriString(serverUrl)
                .path(projectPath + projectId + "/repository/commits")
                .queryParam("ref_name", "master")
                .queryParam("since", startDateTime.toString())
                .queryParam("until", endDateTime.toString())
                .queryParam("per_page", 100)
                .build()
                .encode()
                .toUri();

        WebClient.RequestHeadersSpec<?> headersSpec = webClient.get()
                .uri(gitlabUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        return headersSpec.retrieve().bodyToFlux(GitLabCommit.class);
    }

    public Mono<GitLabCommit> getCommit(Long projectId, String sha) {
        URI gitlabUrl = UriComponentsBuilder.fromUriString(serverUrl)
                .path(projectPath + projectId + "/repository/commits/" + sha)
                .build()
                .encode()
                .toUri();

        WebClient.RequestHeadersSpec<?> headersSpec = webClient.get()
                .uri(gitlabUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        return headersSpec.retrieve().bodyToMono(GitLabCommit.class);
    }

    public Flux<GitLabFileChange> getCommitDiff(Long projectId, String sha) {
        URI gitlabUrl = UriComponentsBuilder.fromUriString(serverUrl)
                .path(projectPath + projectId + "/repository/commits/" + sha + "/diff")
                .queryParam("per_page", 100)
                .build()
                .encode()
                .toUri();

        WebClient.RequestHeadersSpec<?> headersSpec = webClient.get()
                .uri(gitlabUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        return headersSpec.retrieve().bodyToFlux(GitLabFileChange.class);
    }

    public Flux<GitLabFileChange> getMergeRequestDiff(Long projectId, Long mergeRequestIid) {
        URI gitlabUrl = UriComponentsBuilder.fromUriString(serverUrl)
                .path(projectPath + projectId + "/merge_requests/" + mergeRequestIid + "/changes")
                .queryParam("access_raw_diffs", true)
                .queryParam("per_page", 100)
                .build()
                .encode()
                .toUri();

        WebClient.RequestHeadersSpec<?> headersSpec = webClient.get()
                .uri(gitlabUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        return headersSpec.retrieve().bodyToMono(GitLabMergeRequestChange.class).flatMapIterable(GitLabMergeRequestChange::getChanges);
    }

    public Flux<GitLabMergeRequestNote> getMergeRequestNotes(Long projectId, Long mergeRequestIid) {
        URI gitlabUrl = UriComponentsBuilder.fromUriString(serverUrl)
                .path(projectPath + projectId + "/merge_requests/" + mergeRequestIid + "/notes")
                .queryParam("per_page", 100)
                .build()
                .encode()
                .toUri();

        return webClient.get()
                .uri(gitlabUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToFlux(GitLabMergeRequestNote.class);
    }

    public Flux<GitLabIssue> getIssues(Long projectId) {
        URI gitlabUrl = UriComponentsBuilder.fromUriString(serverUrl)
                .path(projectPath + projectId + "/issues")
                .queryParam("per_page", 100)
                .build()
                .encode()
                .toUri();

        return webClient.get()
                .uri(gitlabUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToFlux(GitLabIssue.class);
    }

    public Flux<GitLabIssueNote> getIssueNotes(Long projectId, Long issue_iid) {
        URI gitlabUrl = UriComponentsBuilder.fromUriString(serverUrl)
                .path(projectPath + projectId + "/issues/" + issue_iid + "/notes")
                .queryParam("per_page", 100)
                .build()
                .encode()
                .toUri();

        return webClient.get()
                .uri(gitlabUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToFlux(GitLabIssueNote.class);
    }
}
