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
    public int getDiffScore (@PathVariable("projectId") Long projectId,
                             @PathVariable("merge_request_iid") Long merge_request_iid){
        return scoreService.getDiffScore(projectId, merge_request_iid);
    }
}