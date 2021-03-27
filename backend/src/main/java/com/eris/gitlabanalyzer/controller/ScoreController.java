package com.eris.gitlabanalyzer.controller;

import com.eris.gitlabanalyzer.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

class MergeReturnObject {
    public double mergeScore;
    public double sharedMergeScore;

    public MergeReturnObject(double mergeScore, double sharedMergeScore){
        this.mergeScore = mergeScore;
        this.sharedMergeScore = sharedMergeScore;
    }
}

@RestController
@RequestMapping(path = "/api/v1/data/projects")
public class ScoreController {

    private final ScoreService scoreService;

    @Autowired
    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @GetMapping(path ="/merge_request/{merge_request_id}/diff/score/{scoreProfileId}")
    public double getMergeDiffScore (@PathVariable("merge_request_id") Long merge_request_id,
                                     @PathVariable("scoreProfileId") Long scoreProfileId){
        return scoreService.getMergeDiffScore(merge_request_id, scoreProfileId);
    }
    // gitManagementUserId of 0 return project total for date range
    @GetMapping(path ="/{projectId}/merge_request/user/{gitManagementUserId}/diff/score/{scoreProfileId}")
    public MergeReturnObject getUserMergeScore (@PathVariable("gitManagementUserId") Long gitManagementUserId,
                                                               @PathVariable("scoreProfileId") Long scoreProfileId,
                                                               @PathVariable("projectId") Long projectId,
                                                               @RequestParam("startDateTime")
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDateTime,
                                                               @RequestParam("endDateTime")
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDateTime){
        if(gitManagementUserId != 0L){
            double [] mergeValues = scoreService.getUserMergeScore(gitManagementUserId, projectId, scoreProfileId, startDateTime, endDateTime);
            return new MergeReturnObject(mergeValues[0], mergeValues[1]);
        }else {
            double mergeScore = scoreService.getTotalMergeDiffScore(projectId, scoreProfileId, startDateTime, endDateTime);
            return new MergeReturnObject(mergeScore, 0);
        }

    }

    @GetMapping(path ="/commit/{commitId}/diff/score/{scoreProfileId}")
    public double getCommitDiffScore (@PathVariable("commitId") Long commitId,
                                      @PathVariable("scoreProfileId") Long scoreProfileId){
        return scoreService.getCommitDiffScore(commitId, scoreProfileId);
    }
    // gitManagementUserId of 0 return total commits of project for date range
    @GetMapping(path ="/{projectId}/commit/user/{gitManagementUserId}/diff/score/{scoreProfileId}")
    public double getUserCommitScore (@PathVariable("gitManagementUserId") Long gitManagementUserId,
                                       @PathVariable("scoreProfileId") Long scoreProfileId,
                                       @PathVariable("projectId") Long projectId,
                                       @RequestParam("startDateTime")
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDateTime,
                                       @RequestParam("endDateTime")
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDateTime){
        if(gitManagementUserId != 0L){
            return scoreService.getUserCommitScore(projectId, gitManagementUserId, scoreProfileId, startDateTime, endDateTime);
        } else {
            return scoreService.getTotalCommitDiffScore(projectId, scoreProfileId, startDateTime, endDateTime);
        }
    }

}