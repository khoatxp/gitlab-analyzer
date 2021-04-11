package com.eris.gitlabanalyzer.controller;

import com.eris.gitlabanalyzer.service.ScoreService;
import com.eris.gitlabanalyzer.viewmodel.CommitView;
import com.eris.gitlabanalyzer.viewmodel.MergeRequestView;
import com.eris.gitlabanalyzer.viewmodel.ScoreDigest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

class MergeReturnObject {
    public double mergeScore;
    public double sharedMergeScore;

    public MergeReturnObject(double mergeScore, double sharedMergeScore) {
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

    @GetMapping(path = "/merge_request/{merge_request_id}/diff/score/{scoreProfileId}")
    public double getMergeDiffScore(@PathVariable("merge_request_id") Long merge_request_id,
                                    @PathVariable("scoreProfileId") Long scoreProfileId) {
        return scoreService.getMergeDiffScore(merge_request_id, scoreProfileId);
    }

    // gitManagementUserId of 0 return project total for date range
    @GetMapping(path = "/{projectId}/merge_request/user/{gitManagementUserId}/diff/score/{scoreProfileId}")
    public MergeReturnObject getUserMergeScore(@PathVariable("gitManagementUserId") Long gitManagementUserId,
                                               @PathVariable("scoreProfileId") Long scoreProfileId,
                                               @PathVariable("projectId") Long projectId,
                                               @RequestParam("startDateTime")
                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDateTime,
                                               @RequestParam("endDateTime")
                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDateTime) {
        if (gitManagementUserId != 0L) {
            double[] mergeValues = scoreService.getUserMergeScore(gitManagementUserId, projectId, scoreProfileId, startDateTime, endDateTime);
            return new MergeReturnObject(mergeValues[0], mergeValues[1]);
        } else {
            double mergeScore = scoreService.getTotalMergeDiffScore(projectId, scoreProfileId, startDateTime, endDateTime);
            return new MergeReturnObject(mergeScore, 0);
        }

    }

    @GetMapping(path = "/merge_request/{merge_request_id}/user/{gitManagementUserId}/diff/score/{scoreProfileId}")
    public MergeReturnObject getUserSingleMergeScore(@PathVariable("gitManagementUserId") Long gitManagementUserId,
                                                     @PathVariable("scoreProfileId") Long scoreProfileId,
                                                     @PathVariable("merge_request_id") Long mergeId) {
        if (gitManagementUserId != 0L) {
            double[] mergeValues = scoreService.getUserSingleMergeScore(gitManagementUserId, mergeId, scoreProfileId);
            return new MergeReturnObject(mergeValues[0], mergeValues[1]);
        } else {
            double mergeScore = scoreService.getMergeDiffScore(mergeId, scoreProfileId);
            return new MergeReturnObject(mergeScore, 0);
        }
    }

    @GetMapping(path = "/merge_request/{merge_request_id}/ignore")
    public MergeRequestView toggleIgnoreMergeFromScore(@PathVariable("merge_request_id") Long mergeId) {
        return MergeRequestView.fromMergeRequest(scoreService.toggleIgnoreMergeFromScore(mergeId));
    }


    @GetMapping(path = "/commit/{commitId}/diff/score/{scoreProfileId}")
    public double getCommitDiffScore(@PathVariable("commitId") Long commitId,
                                     @PathVariable("scoreProfileId") Long scoreProfileId) {
        return scoreService.getCommitDiffScore(commitId, scoreProfileId);
    }

    // gitManagementUserId of 0 return total commits of project for date range
    @GetMapping(path = "/{projectId}/commit/user/{gitManagementUserId}/diff/score/{scoreProfileId}")
    public double getUserCommitScore(@PathVariable("gitManagementUserId") Long gitManagementUserId,
                                     @PathVariable("scoreProfileId") Long scoreProfileId,
                                     @PathVariable("projectId") Long projectId,
                                     @RequestParam("startDateTime")
                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDateTime,
                                     @RequestParam("endDateTime")
                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDateTime) {
        if (gitManagementUserId != 0L) {
            return scoreService.getUserCommitScore(projectId, gitManagementUserId, scoreProfileId, startDateTime, endDateTime);
        } else {
            return scoreService.getTotalCommitDiffScore(projectId, scoreProfileId, startDateTime, endDateTime);
        }
    }

    @GetMapping(path = "/commit/{commitId}/ignore")
    public CommitView toggleIgnoreCommitFromScore(@PathVariable("commitId") Long commitId) {
        return CommitView.fromCommit(scoreService.toggleIgnoreCommitFromScore(commitId));
    }

    @GetMapping(path = "/{projectId}/score_digest/user/{gitManagementUserId}/{scoreProfileId}")
    public List<ScoreDigest> getDailyScoreDigest(@PathVariable("projectId") Long projectId,
                                                 @PathVariable("gitManagementUserId") Long gitManagementUserId,
                                                 @PathVariable("scoreProfileId") Long scoreProfileId,
                                                 @RequestParam("startDateTime")
                                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDateTime,
                                                 @RequestParam("endDateTime")
                                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDateTime) {
        return scoreService.getDailyScoreDigest(projectId, gitManagementUserId, scoreProfileId, startDateTime, endDateTime);
    }
}