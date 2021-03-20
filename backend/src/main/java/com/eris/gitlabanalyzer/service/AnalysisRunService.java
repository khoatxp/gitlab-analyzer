package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.AnalysisRun;
import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.model.ScoreProfile;
import com.eris.gitlabanalyzer.model.User;
import com.eris.gitlabanalyzer.repository.AnalysisRunRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class AnalysisRunService {
    private AnalysisRunRepository analysisRunRepository;

    @Autowired
    public void AnalyticsService(AnalysisRunRepository analysisRunRepository) {
        this.analysisRunRepository = analysisRunRepository;
    }

    public void createAnalysisRun(User owner, Project project, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        AnalysisRun analysisRun = new AnalysisRun(
                owner,
                project,
//                scoreProfile, TODO: hookup once implemented
                startDateTime,
                endDateTime
        );
        this.analysisRunRepository.save(analysisRun);
    }
}
