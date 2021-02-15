package com.eris.gitlabanalyzer.controller;

import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.service.AnalyticsService;
import com.eris.gitlabanalyzer.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/projects")
@CrossOrigin() // TODO configure CORS so front end is globally allowed to access routes
public class ProjectController {
    private final ProjectService projectService;
    private final AnalyticsService analyticsService;
    @Autowired
    public ProjectController(ProjectService projectService, AnalyticsService analyticsService){
        this.projectService = projectService;
        this.analyticsService = analyticsService;
    }
    @PostMapping(path = "/analytics")
    public void analyzeProject(
            @RequestBody List<Long> projectIdList,
            @RequestParam(required = true) String serverUrl,
            @RequestParam(required = true) String accessToken){
        analyticsService.saveAllFromGitlab(projectIdList);
    }

    @GetMapping
    public List<Project> getProjects(){
        return projectService.getProjects();
    }
}
