package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.Project;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

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

    public Flux<Project> getProjects(){

        URI gitlabUrl = UriComponentsBuilder.fromUriString(serverUrl)
                .path("/api/v4/projects/")
                .build()
                .encode()
                .toUri();
        WebClient.RequestHeadersSpec<?> headersSpec = webClient.get()
                .uri(gitlabUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        return headersSpec.retrieve().bodyToFlux(Project.class);
    }

}
