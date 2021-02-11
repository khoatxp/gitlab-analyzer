package com.eris.gitlabanalyzer.controller;

import com.eris.gitlabanalyzer.model.GitLabProject;
import com.eris.gitlabanalyzer.service.GitLabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(path = "/api/v1/gitlab")
@CrossOrigin() // TODO configure CORS so front end is globally allowed to access routes
public class GitLabController {

    private final GitLabService gitLabService;

    @Autowired
    public GitLabController(GitLabService gitLabService){
        this.gitLabService = gitLabService;
    }

    //TODO assuming in the future we will have a and id (maybe serverId) to lookup
    // the the server url and token info from the database based on user's selected server
    //@GetMapping(path ="{serverId}/projects")
    // public List<Project> getProjects(@PathVariable("serverId") Long id)
    @GetMapping(path ="/projects")
    public Flux<GitLabProject> getProjects() {
        return gitLabService.getProjects();
    }
}
