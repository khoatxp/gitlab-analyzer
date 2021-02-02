package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.repository.ProjectRepository;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public void getProjectInfo(Long projectId, String serverURL, String accessToken){
        RestTemplate restTemplate = new RestTemplate();

        URI gitlabUrl = UriComponentsBuilder.fromUriString(serverURL)
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
                responseObject.get("web_url").getAsString()
        );

        projectRepository.save(project);
    }


    public void analyzeProjects(List<Long> projectIdList, String serverUrl, String accessToken) {
        for (Long projectId : projectIdList){
            getProjectInfo(projectId, serverUrl, accessToken);
        }
    }
}
