package com.eris.gitlabanalyzer.controller;

import com.eris.gitlabanalyzer.service.AuthService;
import com.eris.gitlabanalyzer.service.NoteService;
import com.eris.gitlabanalyzer.service.ProjectService;
import com.eris.gitlabanalyzer.viewmodel.NoteView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class NoteController {
    private final AuthService authService;
    private final ProjectService projectService;

    private final NoteService noteService;

    @Autowired
    public NoteController(AuthService authService, ProjectService projectService, NoteService noteService) {
        this.authService = authService;
        this.projectService = projectService;
        this.noteService = noteService;
    }

    @GetMapping(path = "/api/v1/{projectId}/merge_request_notes")
    public List<NoteView> getMergeRequestNotes(
            Principal principal,
            @PathVariable("projectId") Long projectId) {

        validatePermission(principal, projectId);
        return noteService.getMergeRequestNotes(projectId);
    }

    @GetMapping(path = "/api/v1/{projectId}/issue_notes")
    public List<NoteView> getIssueNotes(
            Principal principal,
            @PathVariable("projectId") Long projectId) {

        validatePermission(principal, projectId);
        return noteService.getIssueNotes(projectId);
    }

    private void validatePermission(Principal principal, Long projectId) {
        if (!hasProjectPermission(principal, projectId)) {
            throw new AccessDeniedException("User has no permission to see this project.");
        }
    }

    private boolean hasProjectPermission(Principal principal, Long projectId) {
        var user = authService.getLoggedInUser(principal);
        var project = projectService.getProjectById(projectId);
        var server = project.getServer();
        return authService.hasProjectPermission(user.getId(), server.getId(), projectId);
    }
}
