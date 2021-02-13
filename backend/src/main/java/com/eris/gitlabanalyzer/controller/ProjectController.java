package com.eris.gitlabanalyzer.controller;

import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/projects")
@CrossOrigin() // TODO configure CORS so front end is globally allowed to access routes
public class ProjectController {
    private final ProjectService projectService;
    @Autowired
    public ProjectController(ProjectService projectService){
        this.projectService = projectService;
    }
    @PostMapping(path = "/analytics")
    public void analyzeProject(
            @RequestBody List<Long> projectIdList,
            @RequestParam(required = true) String serverUrl,
            @RequestParam(required = true) String accessToken){
        projectService.getProjectAnalytics(projectIdList);
    }

    @GetMapping
    public List<Project> getProjects(){
        return projectService.getProjects();
    }
}
