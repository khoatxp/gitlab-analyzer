package com.eris.gitlabanalyzer.controller;

import com.eris.gitlabanalyzer.model.AnalysisRun;
import com.eris.gitlabanalyzer.model.User;
import com.eris.gitlabanalyzer.repository.AnalysisRunRepository;
import com.eris.gitlabanalyzer.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(path="/api/v1/analysis_run")
public class AnalysisRunController {
    private final AnalysisRunRepository analysisRunRepository;
    private final AuthService authService;

    @Autowired
    public AnalysisRunController(AnalysisRunRepository analysisRunRepository, AuthService authService) {
        this.analysisRunRepository = analysisRunRepository;
        this.authService = authService;
    }

    @GetMapping
    public List<AnalysisRun> getAnalysisRunsByOwnerUserId(Principal currentUser) {
        User user = this.authService.getLoggedInUser(currentUser);
        return this.analysisRunRepository.findAnalysisRunByOwnerUserId(user.getId());
    }
}
