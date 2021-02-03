package com.eris.gitlabanalyzer.controller;

import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProjectController {
    private final ProjectService projectService;
    @Autowired
    public ProjectController(ProjectService projectService){
        this.projectService = projectService;
    }
    @PostMapping("/api/v1/projects/analytics")
    public void analyzeProjects(
            @RequestBody List<Long> projectIdList,
            @RequestParam(required = true) String serverUrl,
            @RequestParam(required = true) String accessToken){
        projectService.analyzeProjects(projectIdList, serverUrl, accessToken);
    }
    @GetMapping("/api/v1/projects")
    public List<Project> getProjects(){
        return projectService.getProjects();
    }


}
