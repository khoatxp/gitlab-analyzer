package com.eris.gitlabanalyzer.controller;
import com.eris.gitlabanalyzer.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;


@RestController
@RequestMapping(path = "/api/v1/data")
public class ScoreController {

    private final ScoreService scoreService;

    @Autowired
    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @GetMapping(path ="/projects/{projectId}/merge_request/{merge_request_iid}/diff/score")
    public int getMergeDiffScore (@PathVariable("projectId") Long projectId,
                             @PathVariable("merge_request_iid") Long merge_request_iid){
        return scoreService.getMergeDiffScore(projectId, merge_request_iid);
    }
    @GetMapping(path ="/projects/{projectId}/merge_requests/score")
    public int getTotalMergeDiffScore (@PathVariable("projectId") Long projectId,
                                       @RequestParam("startDateTime")
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime startDateTime,
                                       @RequestParam("endDateTime")
                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime endDateTime){
        return scoreService.getTotalMergeDiffScore(projectId, startDateTime, endDateTime);
    }

    @GetMapping(path ="/projects/{projectId}/commit/{sha}/diff/score")
    public int getCommitDiffScore (@PathVariable("projectId") Long projectId,
                             @PathVariable("sha") String sha){
        return scoreService.getCommitDiffScore(projectId, sha);
    }
    @GetMapping(path ="/projects/{projectId}/commits/score")
    public int getTotalCommitDiffScore (@PathVariable("projectId") Long projectId,
                                       @RequestParam("startDateTime")
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime startDateTime,
                                       @RequestParam("endDateTime")
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime endDateTime){
        return scoreService.getTotalCommitDiffScore(projectId, startDateTime, endDateTime);
    }
}