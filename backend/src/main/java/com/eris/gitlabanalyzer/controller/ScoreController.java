package com.eris.gitlabanalyzer.controller;
import com.eris.gitlabanalyzer.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


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
    @GetMapping(path ="/projects/{projectId}/commit/{sha}/diff/score")
    public int getCommitDiffScore (@PathVariable("projectId") Long projectId,
                             @PathVariable("sha") String sha){
        return scoreService.getCommitDiffScore(projectId, sha);
    }
}