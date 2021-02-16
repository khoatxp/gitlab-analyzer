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
import java.util.List;

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
                .build()
                .encode()
                .toUri();

        WebClient.RequestHeadersSpec<?> headersSpec = webClient.get()
                .uri(gitlabUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        return headersSpec.retrieve().bodyToFlux(GitLabMergeRequest.class);
    }

    public Flux<GitLabCommit> getMergeRequestCommits(Long projectId, long mergeRequestIid) {
        URI gitlabUrl = UriComponentsBuilder.fromUriString(serverUrl)
                .path(projectPath + projectId + "/merge_requests/" + mergeRequestIid + "/commits")
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

    public Flux<GitLabDiff> getCommitDiff(Long projectId, String sha) {
        URI gitlabUrl = UriComponentsBuilder.fromUriString(serverUrl)
                .path(projectPath + projectId + "/repository/commits/" + sha + "/diff")
                .build()
                .encode()
                .toUri();

        WebClient.RequestHeadersSpec<?> headersSpec = webClient.get()
                .uri(gitlabUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        return headersSpec.retrieve().bodyToFlux(GitLabDiff.class);
    }

    public Mono<List<GitLabDiff>> getMergeRequestDiff(Long projectId, long mergeRequestIid) {
        URI gitlabUrl = UriComponentsBuilder.fromUriString(serverUrl)
                .path(projectPath + projectId + "/merge_requests/" + mergeRequestIid + "/changes")
                .queryParam("access_raw_diffs", true)
                .build()
                .encode()
                .toUri();

        WebClient.RequestHeadersSpec<?> headersSpec = webClient.get()
                .uri(gitlabUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        return headersSpec.retrieve().bodyToMono(GitLabChange.class).map(GitLabChange::getChanges);
    }
}
