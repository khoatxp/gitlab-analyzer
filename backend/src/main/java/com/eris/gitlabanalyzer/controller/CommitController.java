package com.eris.gitlabanalyzer.controller;

import com.eris.gitlabanalyzer.model.Commit;
import com.eris.gitlabanalyzer.service.CommitService;
import com.eris.gitlabanalyzer.viewmodel.CommitAuthorRequestBody;
import com.eris.gitlabanalyzer.viewmodel.CommitAuthorView;
import com.eris.gitlabanalyzer.viewmodel.CommitView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Stream;

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
            @RequestParam(required = false) String state) {
        if (state != null && state.equals("unmapped")) {
            return commitService.getUnmappedCommitAuthors(projectId);
        }
        return commitService.getCommitAuthors(projectId);
    }

    @GetMapping("{projectId}/commits")
    public List<Commit> getCommits(
            @PathVariable("projectId") Long projectId) {
        return commitService.getCommits(projectId);
    }

    @GetMapping("{projectId}/commits/{gitManagementUserId}")
    @GetMapping("/api/v1/{projectId}/commits/orphan/{gitManagementUserId}")
    public Stream<CommitView> getOrphanCommits(
            @PathVariable("projectId") Long projectId,
            @PathVariable("gitManagementUserId") Long gitManagementUserId,
            @RequestParam("startDateTime")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDateTime,
            @RequestParam("endDateTime")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDateTime) {
        List<Commit> commits;
        if (gitManagementUserId == 0L) {
            commits = commitService.getOrphanCommitsInDateRange(projectId, startDateTime, endDateTime);
        } else {
            commits = commitService.getOrphanCommitsOfGitManagementUserInDateRange(projectId, gitManagementUserId, startDateTime, endDateTime);
        }
        return commits.stream().map(CommitView::fromCommit);
    }

    @GetMapping("/api/v1/{projectId}/commits/{gitManagementUserId}")
    public List<Commit> getCommitsOfGitManagementUser(
            @PathVariable("projectId") Long projectId,
            @PathVariable("gitManagementUserId") Long gitManagementUserId) {
        return commitService.getCommitsOfGitManagementUser(projectId, gitManagementUserId);
    }

    @GetMapping("data/projects/{projectId}/commits/user/{gitManagementUserId}/count")
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

    @GetMapping("data/projects/{projectId}/merge_request/{mergeRequestId}/commits/user/{gitManagementUserId}")
    public Stream<CommitView> getCommitsOfGitManagementUserByMergeRequest(@PathVariable("projectId") Long projectId,
                                     @PathVariable("mergeRequestId") Long mergeRequestId,
                                     @PathVariable("gitManagementUserId") Long gitManagementUserId,
                                     @RequestParam("startDateTime")
                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDateTime,
                                     @RequestParam("endDateTime")
                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDateTime){
        if(gitManagementUserId != 0L){
            return commitService.getCommitsOfGitManagementUserInDateRangeByMergeRequestId(mergeRequestId, gitManagementUserId, startDateTime, endDateTime)
                    .stream().map(CommitView::fromCommit);
        } else {
            return commitService.getCommitsInDateRangeByMergeRequestId(mergeRequestId,startDateTime,endDateTime)
                    .stream().map(CommitView::fromCommit);
        }
    }

    @PostMapping("{projectId}/commits/mapping")
    public void mapNewCommitAuthors(
            @PathVariable("projectId") Long projectId,
            @RequestBody List<CommitAuthorRequestBody> commitAuthors) {
        commitService.mapNewCommitAuthors(projectId, commitAuthors);
        // update MR shared status to match mapping
        commitService.setAllSharedMergeRequests(projectId);
    }
}
