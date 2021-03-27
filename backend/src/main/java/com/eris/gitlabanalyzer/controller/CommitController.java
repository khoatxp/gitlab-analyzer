package com.eris.gitlabanalyzer.controller;

import com.eris.gitlabanalyzer.model.Commit;
import com.eris.gitlabanalyzer.service.CommitService;
import com.eris.gitlabanalyzer.viewmodel.CommitAuthorRequestBody;
import com.eris.gitlabanalyzer.viewmodel.CommitAuthorView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/")
public class CommitController {
    private final CommitService commitService;

    @Autowired
    public CommitController(CommitService commitService) {
        this.commitService = commitService;
    }

    @GetMapping("{projectId}/commits/authors")
    public List<CommitAuthorView> getCommitAuthors(
            @PathVariable("projectId") Long projectId,
            @RequestParam(required = false) String state){
        if (state != null && state.equals("unmapped")) {
            return commitService.getUnmappedCommitAuthors(projectId);
        }
        return commitService.getCommitAuthors(projectId);
    }

    @GetMapping("{projectId}/commits")
    public List<Commit> getCommits(
            @PathVariable("projectId") Long projectId){
        return commitService.getCommits(projectId);
    }

    @GetMapping("{projectId}/commits/{gitManagementUserId}")
    public List<Commit> getCommitsOfGitManagementUser(
            @PathVariable("projectId") Long projectId,
            @PathVariable("gitManagementUserId") Long gitManagementUserId){
        return commitService.getCommitsOfGitManagementUser(projectId,(Long)gitManagementUserId);
    }

    @GetMapping("data/projects/{projectId}/commits/user/{gitManagementUserId}")
    public int CommitCount( @PathVariable("projectId") Long projectId,
                                  @PathVariable("gitManagementUserId") Long gitManagementUserId,
                                  @RequestParam("startDateTime")
                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDateTime,
                                  @RequestParam("endDateTime")
                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDateTime){
        if(gitManagementUserId != 0L){
            return commitService.getCommitsOfGitManagementUserInDateRange(projectId, gitManagementUserId, startDateTime, endDateTime).size();

        } else {
            return commitService.getCommitsInDateRange(projectId, startDateTime, endDateTime).size();
        }
    }

    @PostMapping("{projectId}/commits/mapping")
    public void mapNewCommitAuthors(
            @PathVariable("projectId") Long projectId,
            @RequestBody List<CommitAuthorRequestBody> commitAuthors) {
        commitService.mapNewCommitAuthors(projectId,commitAuthors);
        // update MR shared status to match mapping
        commitService.setAllSharedMergeRequests(projectId);
    }
}
