package com.eris.gitlabanalyzer.controller;

import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.model.RawTimeLineProjectData;
import com.eris.gitlabanalyzer.model.Server;
import com.eris.gitlabanalyzer.repository.ProjectRepository;
import com.eris.gitlabanalyzer.repository.ServerRepository;
import com.eris.gitlabanalyzer.repository.UserProjectPermissionRepository;
import com.eris.gitlabanalyzer.repository.UserServerRepository;
import com.eris.gitlabanalyzer.service.AnalyticsService;
import com.eris.gitlabanalyzer.service.AuthService;
import com.eris.gitlabanalyzer.service.ProjectService;
import com.eris.gitlabanalyzer.service.UserServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/projects")
public class ProjectController {
    private final AuthService authService;
    private final ProjectRepository projectRepository;
    private final UserProjectPermissionService userProjectPermissionService;
    private final UserServerService userServerService;
    private final ServerRepository serverRepository;
    private final UserServerRepository userServerRepository;
    private final UserProjectPermissionRepository userProjectPermissionRepository;

    @Autowired
    public ProjectController(AuthService authService, ProjectRepository projectRepository, UserProjectPermissionService userProjectPermissionService, UserServerService userServerService, ServerRepository serverRepository, UserServerRepository userServerRepository, UserProjectPermissionRepository userProjectPermissionRepository) {
        this.authService = authService;
        this.projectRepository = projectRepository;
        this.userProjectPermissionService = userProjectPermissionService;
        this.userServerService = userServerService;
        this.serverRepository = serverRepository;
        this.userServerRepository = userServerRepository;
        this.userProjectPermissionRepository = userProjectPermissionRepository;
    }

    @GetMapping(path = "/{projectId}/rawdata")
    public RawTimeLineProjectData analyzeProject(
            Principal principal,
            @PathVariable("projectId") Long projectId,
            @RequestParam("startDateTime")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDateTime,
            @RequestParam("endDateTime")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDateTime) {
        var user = authService.getLoggedInUser(principal);
        var project = projectRepository.findProjectById(projectId).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find project."));
        var server = project.getServer();
        boolean hasPermission = userProjectPermissionService.userHasPermissionToProject(user, server, project);
        if (!hasPermission) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User don't have permission to access requested project");
        }
        var userServer = userServerService.getUserServer(user, server.getId()).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find UserServer."));
        var projectService = new ProjectService(this.projectRepository, this.serverRepository, this.userServerRepository,
                this.userProjectPermissionRepository, server.getServerUrl(), userServer.getAccessToken());
        return projectService.getTimeLineProjectData(projectId, startDateTime, endDateTime);
    }

    @PostMapping(path = "/analytics")
    public void saveAllFromGitlab(
            Principal principal,
            @RequestBody List<Long> projectIdList,
            @RequestParam("startDateTime")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDateTime,
            @RequestParam("endDateTime")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDateTime){
        analyticsService.saveAllFromGitlab(projectIdList, startDateTime, endDateTime);
    }
}
