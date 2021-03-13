package com.eris.gitlabanalyzer.controller;

import com.eris.gitlabanalyzer.service.CommitService;
import com.eris.gitlabanalyzer.viewmodel.CommitAuthorRequestBody;
import com.eris.gitlabanalyzer.viewmodel.CommitAuthorView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommitController {
    private final CommitService commitService;

    @Autowired
    public CommitController(CommitService commitService) {
        this.commitService = commitService;
    }

    @GetMapping("/api/v1/{projectId}/commits/authors")
    public List<CommitAuthorView> getCommitAuthors(
            @PathVariable("projectId") Long projectId,
            @RequestParam(required = false) String state){
        if (state != null && state.equals("unmapped")) {
            return commitService.getUnmappedCommitAuthors(projectId);
        }
        return commitService.getCommitAuthors(projectId);
    }

    @PostMapping("api/v1/{projectId}/commits/mapping")
    public void mapNewCommitAuthors(
            @PathVariable("projectId") Long projectId,
            @RequestBody List<CommitAuthorRequestBody> commitAuthors) {
        commitService.mapNewCommitAuthors(projectId,commitAuthors);
    }
}
