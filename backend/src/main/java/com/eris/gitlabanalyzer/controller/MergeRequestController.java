package com.eris.gitlabanalyzer.controller;

import com.eris.gitlabanalyzer.service.MergeRequestService;
import com.eris.gitlabanalyzer.viewmodel.MergeRequestView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/api/v1/data/projects")
public class MergeRequestController {

    private final MergeRequestService mergeRequestService;

    @Autowired
    public MergeRequestController(MergeRequestService mergeRequestService) {
        this.mergeRequestService = mergeRequestService;
    }

    @GetMapping(path ="{projectId}/merge_request/user/{gitManagementUserId}/count")
    public int MergeRequestCount( @PathVariable("projectId") Long projectId,
                                  @PathVariable("gitManagementUserId") Long gitManagementUserId,
                                  @RequestParam("startDateTime")
                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDateTime,
                                  @RequestParam("endDateTime")
                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDateTime){
        if(gitManagementUserId != 0L){
            return mergeRequestService.getMergeRequestsByProjectIdAndGitManagementUserId(projectId, gitManagementUserId,startDateTime, endDateTime).size();

        } else {
            return mergeRequestService.getMergeRequestsByProjectId(projectId, startDateTime, endDateTime).size();
        }
    }

    @GetMapping(path ="{projectId}/merge_request/user/{gitManagementUserId}")
    public Stream<MergeRequestView> MergeRequest(@PathVariable("projectId") Long projectId,
                                                 @PathVariable("gitManagementUserId") Long gitManagementUserId,
                                                 @RequestParam("startDateTime")
                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDateTime,
                                                 @RequestParam("endDateTime")
                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDateTime){
        if(gitManagementUserId != 0L){
            return mergeRequestService.getMergeRequestsWhereGitManagementUserHasCommitsIn(projectId, gitManagementUserId, startDateTime, endDateTime)
                    .stream().map(MergeRequestView::fromMergeRequest);

        } else {
            return mergeRequestService.getMergeRequestsByProjectId(projectId, startDateTime, endDateTime)
                    .stream().map(MergeRequestView::fromMergeRequest);
        }
    }
}
