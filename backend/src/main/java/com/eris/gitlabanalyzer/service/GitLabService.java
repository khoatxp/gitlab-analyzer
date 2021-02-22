package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.gitlabresponse.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.HashMap;

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
        String gitlabUrl = UriComponentsBuilder.fromUriString(serverUrl)
                .path(projectPath + projectId + "/members")
                .queryParam("per_page", 100)
                .build()
                .encode()
                .toUri()
                .toString();

        return fetchPages(gitlabUrl).flatMap(response -> response.bodyToFlux(GitLabMember.class));
    }

    public Flux<GitLabMergeRequest> getMergeRequests(Long projectId, ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
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
        String gitlabUrl = UriComponentsBuilder.fromUriString(serverUrl)
                .path(projectPath + projectId + "/merge_requests/" + mergeRequestIid + "/commits")
                .queryParam("per_page", 100)
                .build()
                .encode()
                .toUri()
                .toString();

        return fetchPages(gitlabUrl).flatMap(response -> response.bodyToFlux(GitLabCommit.class));
    }

    public Flux<GitLabCommit> getCommits(Long projectId, ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
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
        String gitlabUrl = UriComponentsBuilder.fromUriString(serverUrl)
                .path(projectPath + projectId + "/repository/commits/" + sha + "/diff")
                .queryParam("per_page", 100)
                .build()
                .encode()
                .toUri()
                .toString();

        return fetchPages(gitlabUrl).flatMap(response -> response.bodyToFlux(GitLabFileChange.class));
    }

    public Flux<GitLabFileChange> getMergeRequestDiff(Long projectId, Long mergeRequestIid) {
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
