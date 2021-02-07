package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.Member;
import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.repository.MemberRepository;
import com.eris.gitlabanalyzer.repository.ProjectRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final WebClient webClient;

    public ProjectService(ProjectRepository projectRepository, MemberRepository memberRepository) {
        this.projectRepository = projectRepository;
        this.memberRepository = memberRepository;
        this.webClient = WebClient.create();
    }

    public Flux<Project> getProjects(String serverUrl, String accessToken){
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

    public Project getProjectInfo(Long projectId, String serverUrl, String accessToken){
        RestTemplate restTemplate = new RestTemplate();

        URI gitlabUrl = UriComponentsBuilder.fromUriString(serverUrl)
                .path("/api/v4/projects/"+projectId)
                .queryParam("private_token",accessToken)
                .build()
                .encode()
                .toUri();

        String response = restTemplate.getForObject(gitlabUrl, String.class);
        JsonObject responseObject = new JsonParser().parse(response).getAsJsonObject();

        Project project = new Project(
                projectId,
                responseObject.get("name").getAsString(),
                responseObject.get("name_with_namespace").getAsString(),
                serverUrl,
                responseObject.get("web_url").getAsString()
        );
        // TODO Check if project already exists
        return projectRepository.save(project);
    }

    public void getMemberInfo(Long projectId, String serverUrl, String accessToken, Project project){
        RestTemplate restTemplate = new RestTemplate();

        URI gitlabUrl = UriComponentsBuilder.fromUriString(serverUrl)
                .path("/api/v4/projects/"+projectId+"/members")
                .queryParam("private_token",accessToken)
                .build()
                .encode()
                .toUri();

        String response = restTemplate.getForObject(gitlabUrl, String.class);
        JsonArray responseObjectArray = new JsonParser().parse(response).getAsJsonArray();
        for(int i=0; i< responseObjectArray.size(); i++){
            JsonObject responseObject = responseObjectArray.get(i).getAsJsonObject();
            Member member = new Member(
                    responseObject.get("id").getAsLong(),
                    responseObject.get("username").getAsString(),
                    responseObject.get("name").getAsString(),
                    responseObject.get("access_level").getAsInt(),
                    project
            );
            System.out.println(memberRepository.save(member));
        }
    }


    public void analyzeProjects(List<Long> projectIdList, String serverUrl, String accessToken) {
        for (Long projectId : projectIdList){
            Project project = getProjectInfo(projectId, serverUrl, accessToken);
            System.out.println(project);
            getMemberInfo(projectId, serverUrl, accessToken, project);
        }
    }

    public List<Project> getProjects() {
        return projectRepository.findAll();
    }
}
