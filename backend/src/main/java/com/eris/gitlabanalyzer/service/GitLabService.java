package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.GitLabMember;
import com.eris.gitlabanalyzer.model.GitLabProject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@Service
public class GitLabService {
    private final WebClient webClient;

    @Value("${gitlab.SERVER_URL}")
    String serverUrl;

    @Value("${gitlab.ACCESS_TOKEN}")
    String accessToken;

    public GitLabService() {
        this.webClient = WebClient.create();
    }

    public Flux<GitLabProject> getProjects(){
        URI gitlabUrl = UriComponentsBuilder.fromUriString(serverUrl)
                .path("/api/v4/projects/")
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
                .path("/api/v4/projects/" + projectId)
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
                .path("/api/v4/projects/"+projectId+"/members")
                .build()
                .encode()
                .toUri();

        WebClient.RequestHeadersSpec<?> headersSpec = webClient.get()
                .uri(gitlabUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        return headersSpec.retrieve().bodyToFlux(GitLabMember.class);
    }

}
