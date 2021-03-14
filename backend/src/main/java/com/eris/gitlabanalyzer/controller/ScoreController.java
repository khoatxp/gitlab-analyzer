package com.eris.gitlabanalyzer.controller;

import com.eris.gitlabanalyzer.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;


@RestController
@RequestMapping(path = "/api/v1/data")
public class ScoreController {

    private final ScoreService scoreService;

    @Autowired
    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @GetMapping(path ="/projects/merge_request/{merge_request_id}/diff/score")
    public double getMergeDiffScore (@PathVariable("merge_request_id") Long merge_request_id){
        return scoreService.getMergeDiffScore(merge_request_id);
    }
    @GetMapping(path ="/projects/{projectId}/merge_requests/score")
    public double getTotalMergeDiffScore (@PathVariable("projectId") Long projectId,
                                       @RequestParam("startDateTime")
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDateTime,
                                       @RequestParam("endDateTime")
                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDateTime){
        return scoreService.getTotalMergeDiffScore(projectId, startDateTime, endDateTime);
    }

    @GetMapping(path ="/projects/commit/{commitId}/diff/score")
    public double getCommitDiffScore (@PathVariable("commitId") Long commitId){
        return scoreService.getCommitDiffScore(commitId);
    }
    @GetMapping(path ="/projects/{projectId}/commits/score")
    public double getTotalCommitDiffScore (@PathVariable("projectId") Long projectId,
                                       @RequestParam("startDateTime")
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDateTime,
                                       @RequestParam("endDateTime")
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDateTime){
        return scoreService.getTotalCommitDiffScore(projectId, startDateTime, endDateTime);
    }
}