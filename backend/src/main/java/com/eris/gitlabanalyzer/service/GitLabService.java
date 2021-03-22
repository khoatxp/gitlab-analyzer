package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.error.GitLabServiceConfigurationException;
import com.eris.gitlabanalyzer.model.gitlabresponse.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.HashMap;

// NOTE: do not make this an auto wired @Service.
// This class needs to be instantiated with correct serverUrl and accessToken OR
// instantiated with no arg constructor and serverUrl, accessToken set via setters
// The injected GitLabService you see in various places is a request scope bean that is created in GitLabServiceConfig
// and set in a GitLabServiceConfigInterceptor preHandle HandlerInterceptor
@Getter
public class GitLabService {
    private final WebClient webClient;
    private final String projectPath = "api/v4/projects/";

    @Setter private String serverUrl;
    @Setter private String accessToken;

    public GitLabService(String serverUrl, String accessToken) {
        this.webClient = WebClient.create();
        this.serverUrl = serverUrl;
        this.accessToken = accessToken;
    }

    public GitLabService() {
        this.webClient = WebClient.create();
    }

    private void validateConfiguration() {
        if (serverUrl == null) {
            throw new GitLabServiceConfigurationException("GitLabService not instantiated correctly, serverUrl is null");
        }
        if (accessToken == null) {
            throw new GitLabServiceConfigurationException("GitLabService not instantiated correctly, accessToken is null");
        }
    }

    public Flux<GitLabProject> getProjects(){
        validateConfiguration();
        String gitlabUrl = UriComponentsBuilder.fromUriString(serverUrl)
                .path(projectPath)
                .queryParam("per_page", 100)
                .build()
                .encode()
                .toUri()
                .toString();

        return fetchPages(gitlabUrl).flatMap(response -> response.bodyToFlux(GitLabProject.class));
    }

    public Mono<GitLabProject> getProject(Long projectId) {
        validateConfiguration();
        String gitlabUrl = UriComponentsBuilder.fromUriString(serverUrl)
                .path(projectPath + projectId)
                .build()
                .encode()
                .toUri()
                .toString();

        var headersSpec = authorizedGetRequestHeadersSpec(gitlabUrl);
        return headersSpec.retrieve().bodyToMono(GitLabProject.class);
    }

    public Flux<GitLabMember> getMembers(Long projectId) {
        validateConfiguration();
        String gitlabUrl = UriComponentsBuilder.fromUriString(serverUrl)
                .path(projectPath + projectId + "/members")
                .queryParam("per_page", 100)
                .build()
                .encode()
                .toUri()
                .toString();

        return fetchPages(gitlabUrl).flatMap(response -> response.bodyToFlux(GitLabMember.class));
    }

    public Flux<GitLabMergeRequest> getMergeRequests(Long projectId, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        validateConfiguration();
        String gitlabUrl = UriComponentsBuilder.fromUriString(serverUrl)
                .path(projectPath + projectId + "/merge_requests")
                .queryParam("state", "merged")
                .queryParam("created_after", startDateTime.toInstant().toString())
                .queryParam("updated_before", endDateTime.toInstant().toString())
                .queryParam("per_page", 100)
                .build()
                .encode()
                .toUri()
                .toString();

        return fetchPages(gitlabUrl).flatMap(response -> response.bodyToFlux(GitLabMergeRequest.class));
    }

    public Flux<GitLabCommit> getMergeRequestCommits(Long projectId, Long mergeRequestIid) {
        validateConfiguration();
        String gitlabUrl = UriComponentsBuilder.fromUriString(serverUrl)
                .path(projectPath + projectId + "/merge_requests/" + mergeRequestIid + "/commits")
                .queryParam("per_page", 100)
                .build()
                .encode()
                .toUri()
                .toString();

        return fetchPages(gitlabUrl).flatMap(response -> response.bodyToFlux(GitLabCommit.class));
    }

    public Flux<GitLabCommit> getCommits(Long projectId, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        validateConfiguration();
        String gitlabUrl = UriComponentsBuilder.fromUriString(serverUrl)
                .path(projectPath + projectId + "/repository/commits")
                .queryParam("since", startDateTime.toInstant().toString())
                .queryParam("until", endDateTime.toInstant().toString())
                .queryParam("per_page", 100)
                .build()
                .encode()
                .toUri()
                .toString();

        return fetchPages(gitlabUrl).flatMap(response -> response.bodyToFlux(GitLabCommit.class));
    }

    public Mono<GitLabCommit> getCommit(Long projectId, String sha) {
        validateConfiguration();
        String gitlabUrl = UriComponentsBuilder.fromUriString(serverUrl)
                .path(projectPath + projectId + "/repository/commits/" + sha)
                .build()
                .encode()
                .toUri()
                .toString();

        var headersSpec = authorizedGetRequestHeadersSpec(gitlabUrl);
        return headersSpec.retrieve().bodyToMono(GitLabCommit.class);
    }

    public Flux<GitLabFileChange> getCommitDiff(Long projectId, String sha) {
        validateConfiguration();
        String gitlabUrl = UriComponentsBuilder.fromUriString(serverUrl)
                .path(projectPath + projectId + "/repository/commits/" + sha + "/diff")
                .queryParam("per_page", 100)
                .build()
                .encode()
                .toUri()
                .toString();

        return fetchPages(gitlabUrl).flatMap(response -> response.bodyToFlux(GitLabFileChange.class));
    }

    public Flux<GitLabCommitComment> getCommitComments(Long projectId, String sha) {
        validateConfiguration();
        String gitlabUrl = UriComponentsBuilder.fromUriString(serverUrl)
                .path(projectPath + projectId + "/repository/commits/" + sha + "/comments")
                .queryParam("per_page", 100)
                .build()
                .encode()
                .toUri()
                .toString();

        return fetchPages(gitlabUrl).flatMap(response -> response.bodyToFlux(GitLabCommitComment.class));
    }

    public Flux<GitLabFileChange> getMergeRequestDiff(Long projectId, Long mergeRequestIid) {
        validateConfiguration();
        String gitlabUrl = UriComponentsBuilder.fromUriString(serverUrl)
                .path(projectPath + projectId + "/merge_requests/" + mergeRequestIid + "/changes")
                .queryParam("access_raw_diffs", true)
                .queryParam("per_page", 100)
                .build()
                .encode()
                .toUri()
                .toString();

        var headersSpec = authorizedGetRequestHeadersSpec(gitlabUrl);
        return headersSpec.retrieve().bodyToMono(GitLabMergeRequestChange.class).flatMapIterable(GitLabMergeRequestChange::getChanges);
    }

    public Flux<GitLabMergeRequestNote> getMergeRequestNotes(Long projectId, Long mergeRequestIid) {
        validateConfiguration();
        String gitlabUrl = UriComponentsBuilder.fromUriString(serverUrl)
                .path(projectPath + projectId + "/merge_requests/" + mergeRequestIid + "/notes")
                .queryParam("per_page", 100)
                .build()
                .encode()
                .toUri()
                .toString();

        return fetchPages(gitlabUrl)
                .flatMap(response -> response.bodyToFlux(GitLabMergeRequestNote.class));
    }

    public Flux<GitLabIssue> getIssues(Long projectId, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        validateConfiguration();
        String gitlabUrl = UriComponentsBuilder.fromUriString(serverUrl)
                .path(projectPath + projectId + "/issues")
                .queryParam("created_after", startDateTime.toInstant().toString())
                .queryParam("updated_before", endDateTime.toInstant().toString())
                .queryParam("per_page", 100)
                .build()
                .encode()
                .toUri()
                .toString();

        return fetchPages(gitlabUrl)
                .flatMap(response -> response.bodyToFlux(GitLabIssue.class));
    }

    public Flux<GitLabIssueNote> getIssueNotes(Long projectId, Long issue_iid) {
        validateConfiguration();
        String gitlabUrl = UriComponentsBuilder.fromUriString(serverUrl)
                .path(projectPath + projectId + "/issues/" + issue_iid + "/notes")
                .queryParam("per_page", 100)
                .build()
                .encode()
                .toUri()
                .toString();

        return fetchPages(gitlabUrl)
                .flatMap(response -> response.bodyToFlux(GitLabIssueNote.class));
    }

    // recursively make a request for the next page and then return a collection of response
    private Flux<ClientResponse> fetchPages(String url) {
        var headersSpec = authorizedGetRequestHeadersSpec(url);
        return headersSpec.exchangeToFlux(response -> {
            var nextPage = getResponseHeaderNextLink(response);

            if (nextPage != null) {
                return Flux.just(response).concatWith(fetchPages(nextPage));
            }

            return Flux.just(response);
        });
    }

    private WebClient.RequestHeadersSpec<?> authorizedGetRequestHeadersSpec(String url) {
        return webClient.get()
            .uri(url)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
    }

    // Based on https://github.com/eclipse/egit-github/blob/master/org.eclipse.egit.github.core/src/org/eclipse/egit/github/core/client/PageLinks.java
    private String getResponseHeaderNextLink(ClientResponse response) {
        if (response != null) {
            var headerLink = response.headers().asHttpHeaders().getFirst(HttpHeaders.LINK);
            if (headerLink != null) {
                var relUrls = getUrlsFromHeaderLink(headerLink);
                return relUrls.get("next");
            }
        }
        return null;
    }

    // Based on https://github.com/eclipse/egit-github/blob/master/org.eclipse.egit.github.core/src/org/eclipse/egit/github/core/client/PageLinks.java
    private HashMap<String, String> getUrlsFromHeaderLink(String headerLink) {
        HashMap<String, String> relUrls = new HashMap<>();
        String[] links = headerLink.split(",");
        for (String link : links) {
            String[] segments = link.split(";");
            if (segments.length < 2)
                continue;

            String linkPart = segments[0].trim();
            if (!linkPart.startsWith("<") || !linkPart.endsWith(">"))
                continue;

            var url = linkPart.substring(1, linkPart.length() - 1); // get the url string inside the "<" & ">"
            for (int i = 1; i < segments.length; i++) { // check the remaining string part for "rel" key
                String[] rel = segments[i].trim().split("=");
                if (rel.length < 2 || !"rel".equals(rel[0]))
                    continue;

                String relValue = rel[1];
                if (relValue.startsWith("\"") && relValue.endsWith("\""))
                    relValue = relValue.substring(1, relValue.length() - 1); // get the rel string inside the double quotes

                relUrls.put(relValue,  url);
            }
        }

        return relUrls;
    }
}
