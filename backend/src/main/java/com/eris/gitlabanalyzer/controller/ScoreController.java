package com.eris.gitlabanalyzer.controller;

import com.eris.gitlabanalyzer.dataprocessing.CalculateDiffMetrics;
import com.eris.gitlabanalyzer.dataprocessing.DiffScoreCalculator;
import com.eris.gitlabanalyzer.repository.CommitRepository;
import com.eris.gitlabanalyzer.repository.MergeRequestRepository;
import com.eris.gitlabanalyzer.repository.ProjectRepository;
import com.eris.gitlabanalyzer.service.AuthService;
import com.eris.gitlabanalyzer.service.ScoreService;
import com.eris.gitlabanalyzer.service.UserServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.OffsetDateTime;


@RestController
@RequestMapping(path = "/api/v1/data")
public class ScoreController {

    private final AuthService authService;
    private final UserServerService userServerService;
    private final ProjectRepository projectRepository;
    private final DiffScoreCalculator diffScoreCalculator;
    private final CalculateDiffMetrics calculateDiffMetrics;
    private final MergeRequestRepository mergeRequestRepository;
    private final CommitRepository commitRepository;

    @Autowired
    public ScoreController(AuthService authService, UserServerService userServerService,
                           ProjectRepository projectRepository,
                           DiffScoreCalculator diffScoreCalculator, CalculateDiffMetrics calculateDiffMetrics, MergeRequestRepository mergeRequestRepository,
                           CommitRepository commitRepository) {
        this.authService = authService;
        this.userServerService = userServerService;
        this.projectRepository = projectRepository;
        this.diffScoreCalculator = diffScoreCalculator;
        this.calculateDiffMetrics = calculateDiffMetrics;
        this.mergeRequestRepository = mergeRequestRepository;
        this.commitRepository = commitRepository;
    }

    public ScoreService initScoreService(Principal principal, Long projectId) {
        var user = authService.getLoggedInUser(principal);
        var project = projectRepository.findProjectById(projectId).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find project."));
        var server = project.getServer();
        var userServer = userServerService.getUserServer(user, server.getId()).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find UserServer."));
        var scoreService = new ScoreService(this.diffScoreCalculator, this.calculateDiffMetrics, this.mergeRequestRepository,
                this.commitRepository, server.getServerUrl(), userServer.getAccessToken());
        return scoreService;
    }

    @GetMapping(path ="/projects/{projectId}/merge_request/{merge_request_id}/diff/score")
    public int getMergeDiffScore (Principal principal, @PathVariable("projectId") Long projectId,
                             @PathVariable("merge_request_id") Long merge_request_id){
        var scoreService = initScoreService(principal, projectId);
        return scoreService.getMergeDiffScore(projectId, merge_request_id);
    }
    @GetMapping(path ="/projects/{projectId}/merge_requests/score")
    public int getTotalMergeDiffScore (Principal principal,
                                       @PathVariable("projectId") Long projectId,
                                       @RequestParam("startDateTime")
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDateTime,
                                       @RequestParam("endDateTime")
                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDateTime){
        var scoreService = initScoreService(principal, projectId);
        return scoreService.getTotalMergeDiffScore(projectId, startDateTime, endDateTime);
    }

    @GetMapping(path ="/projects/{projectId}/commit/{commitId}/diff/score")
    public int getCommitDiffScore (Principal principal, @PathVariable("projectId") Long projectId,
                             @PathVariable("commitId") Long commitId){
        var scoreService = initScoreService(principal, projectId);
        return scoreService.getCommitDiffScore(projectId, commitId);
    }
    @GetMapping(path ="/projects/{projectId}/commits/score")
    public int getTotalCommitDiffScore (Principal principal,
                                        @PathVariable("projectId") Long projectId,
                                       @RequestParam("startDateTime")
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDateTime,
                                       @RequestParam("endDateTime")
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDateTime){
        var scoreService = initScoreService(principal, projectId);
        return scoreService.getTotalCommitDiffScore(projectId, startDateTime, endDateTime);
    }
}