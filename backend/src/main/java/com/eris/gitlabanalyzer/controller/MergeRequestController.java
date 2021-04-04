package com.eris.gitlabanalyzer.controller;

import com.eris.gitlabanalyzer.service.MergeRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

@RestController
@RequestMapping(path = "/api/v1/data/projects")
public class MergeRequestController {

    private final MergeRequestService mergeRequestService;

    @Autowired
    public MergeRequestController(MergeRequestService mergeRequestService) {
        this.mergeRequestService = mergeRequestService;
    }

    @GetMapping(path ="{projectId}/merge_request/user/{gitManagementUserId}")
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
}
