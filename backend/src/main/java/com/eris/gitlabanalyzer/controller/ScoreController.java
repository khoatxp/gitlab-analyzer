package com.eris.gitlabanalyzer.controller;

import com.eris.gitlabanalyzer.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;


@RestController
@RequestMapping(path = "/api/v1/data/projects")
public class ScoreController {

    private final ScoreService scoreService;

    @Autowired
    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }
    // todo remove once we are passing profileId with every call
    @GetMapping(path ="/merge_request/{merge_request_id}/diff/score")
    public double getMergeDiffScore (@PathVariable("merge_request_id") Long merge_request_id){
        return scoreService.getMergeDiffScore(merge_request_id, 0L);
    }
    // todo remove once we are passing profileId with every call
    @GetMapping(path ="/{projectId}/merge_requests/score")
    public double getTotalMergeDiffScore (@PathVariable("projectId") Long projectId,
                                       @RequestParam("startDateTime")
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDateTime,
                                       @RequestParam("endDateTime")
                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDateTime){
        return scoreService.getTotalMergeDiffScore(projectId, 0L, startDateTime, endDateTime);
    }

    @GetMapping(path ="/merge_request/{merge_request_id}/diff/score/{scoreProfileId}")
    public double getMergeDiffScore (@PathVariable("merge_request_id") Long merge_request_id,
                                     @PathVariable("scoreProfileId") Long scoreProfileId){
        return scoreService.getMergeDiffScore(merge_request_id, scoreProfileId);
    }

    @GetMapping(path ="/{projectId}/merge_requests/score/{scoreProfileId}")
    public double getTotalMergeDiffScore (@PathVariable("projectId") Long projectId,
                                       @PathVariable("scoreProfileId") Long scoreProfileId,
                                       @RequestParam("startDateTime")
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDateTime,
                                       @RequestParam("endDateTime")
                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDateTime){
        return scoreService.getTotalMergeDiffScore(projectId, scoreProfileId, startDateTime, endDateTime);
    }

    @GetMapping(path ="/{projectId}/merge_request/user/{gitManagementUserId}/diff/score/{scoreProfileId}")
    public double[] getUserMergeScore (@PathVariable("gitManagementUserId") Long gitManagementUserId,
                                                  @PathVariable("scoreProfileId") Long scoreProfileId,
                                                  @PathVariable("projectId") Long projectId,
                                                  @RequestParam("startDateTime")
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDateTime,
                                                  @RequestParam("endDateTime")
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDateTime){
        return scoreService.getMergeUserScore(gitManagementUserId, projectId, scoreProfileId, startDateTime, endDateTime);
    }

    // todo remove once we are passing profileId with every call
    @GetMapping(path ="/commit/{commitId}/diff/score")
    public double getCommitDiffScore (@PathVariable("commitId") Long commitId){
        return scoreService.getCommitDiffScore(commitId, 0L);
    }

    // todo remove once we are passing profileId with every call
    @GetMapping(path ="/{projectId}/commits/score")
    public double getTotalCommitDiffScore (@PathVariable("projectId") Long projectId,
                                       @RequestParam("startDateTime")
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDateTime,
                                       @RequestParam("endDateTime")
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDateTime){
        return scoreService.getTotalCommitDiffScore(projectId, 0L, startDateTime, endDateTime);
    }

    @GetMapping(path ="/commit/{commitId}/diff/score/{scoreProfileId}")
    public double getCommitDiffScore (@PathVariable("commitId") Long commitId,
                                      @PathVariable("scoreProfileId") Long scoreProfileId){
        return scoreService.getCommitDiffScore(commitId, scoreProfileId);
    }
    @GetMapping(path ="/{projectId}/commit/user/{gitManagementUserId}/diff/score/{scoreProfileId}")
    public double getUserCommitScore (@PathVariable("gitManagementUserId") Long gitManagementUserId,
                                       @PathVariable("scoreProfileId") Long scoreProfileId,
                                       @PathVariable("projectId") Long projectId,
                                       @RequestParam("startDateTime")
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDateTime,
                                       @RequestParam("endDateTime")
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDateTime){
        return scoreService.getUserCommitScore(projectId, gitManagementUserId, scoreProfileId, startDateTime, endDateTime);
    }

    @GetMapping(path ="/{projectId}/commits/score/{scoreProfileId}")
    public double getTotalCommitDiffScore (@PathVariable("projectId") Long projectId,
                                       @PathVariable("scoreProfileId") Long scoreProfileId,
                                       @RequestParam("startDateTime")
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDateTime,
                                       @RequestParam("endDateTime")
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDateTime){
        return scoreService.getTotalCommitDiffScore(projectId, scoreProfileId, startDateTime, endDateTime);
    }
}