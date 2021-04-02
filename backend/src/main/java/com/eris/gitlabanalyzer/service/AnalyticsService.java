package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.AnalysisRun;
import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.model.User;
import com.eris.gitlabanalyzer.repository.AnalysisRunRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnalyticsService {
    private final ProjectService projectService;
    private final GitManagementUserService gitManagementUserService;
    private final MergeRequestService mergeRequestService;
    private final CommitService commitService;
    private final IssueService issueService;
    private final AnalysisRunService analysisRunService;
    private final AnalysisRunRepository analysisRunRepository;

    public AnalyticsService(
            ProjectService projectService,
            GitManagementUserService gitManagementUserService,
            MergeRequestService mergeRequestService,
            CommitService commitService,
            IssueService issueService,
            AnalysisRunService analysisRunService,
            AnalysisRunRepository analysisRunRepository) {
        this.projectService = projectService;
        this.gitManagementUserService = gitManagementUserService;
        this.mergeRequestService = mergeRequestService;
        this.commitService = commitService;
        this.issueService = issueService;
        this.analysisRunService = analysisRunService;
        this.analysisRunRepository = analysisRunRepository;
    }

    public List<Long> saveAllFromGitlab(User user, Long serverId, List<Long> gitLabProjectIdList, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        List<AnalysisRun> analysisRuns = saveProjectsAndAnalysisRuns(user, serverId, gitLabProjectIdList, startDateTime, endDateTime);
        return saveProjectDataForAnalysisRuns(analysisRuns);
    }

    public List<AnalysisRun> saveProjectsAndAnalysisRuns(User user, Long serverId, List<Long> gitLabProjectIdList, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        List<AnalysisRun> analysisRuns = new ArrayList<>();
        gitLabProjectIdList.forEach(gitLabProjectId -> {
            Project project = projectService.saveProjectInfo(user, serverId, gitLabProjectId);
            analysisRuns.add(this.analysisRunService.createAnalysisRun(user, project, AnalysisRun.Status.InProgress, startDateTime, endDateTime));
        });
        return analysisRuns;
    }

    public List<Long> saveProjectDataForAnalysisRuns(List<AnalysisRun> analysisRuns) {
        List<Long> projectIds = new ArrayList<>();
        analysisRuns.forEach(analysisRun -> {
            try {
                var project = analysisRun.getProject();
                var startDateTime = analysisRun.getStartDateTime();
                var endDateTime = analysisRun.getEndDateTime();
                gitManagementUserService.saveGitManagementUserInfo(project);
                mergeRequestService.saveMergeRequestInfo(project, startDateTime, endDateTime);
                commitService.saveCommitInfo(project, startDateTime, endDateTime);
                issueService.saveIssueInfo(project, startDateTime, endDateTime);
                projectIds.add(project.getId());
                analysisRun.setStatus(AnalysisRun.Status.Completed);
                analysisRunRepository.save(analysisRun);
            } catch(Exception e) {
                System.err.println(e);
                analysisRun.setStatus(AnalysisRun.Status.Error);
                analysisRunRepository.save(analysisRun);
            }
        });
        return projectIds;
    }
}
