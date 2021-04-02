package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.AnalysisRun;
import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.model.User;
import com.eris.gitlabanalyzer.repository.AnalysisRunRepository;
import com.eris.gitlabanalyzer.viewmodel.AnalysisRunView;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

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

    public Stream<AnalysisRunView> saveProjectsAndAnalysisRuns(User user, Long serverId, List<Long> gitLabProjectIdList, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        List<AnalysisRun> analysisRuns = new ArrayList<>();
        gitLabProjectIdList.forEach(gitLabProjectId -> {
            try{
                Project project = projectService.saveProjectInfo(user, serverId, gitLabProjectId);
                analysisRuns.add(this.analysisRunService.createAnalysisRun(user, project, AnalysisRun.Status.InProgress, startDateTime, endDateTime));
            } catch(Exception e){
                System.out.println("yo"+e);
            }

        });
        return analysisRuns.stream().map(AnalysisRunView::fromAnalysisRun);
    }

    public List<Long> saveProjectDataForAnalysisRuns(List<Long> analysisRunIds) {
        List<Long> projectIds = new ArrayList<>();
        List<AnalysisRun> analysisRuns = analysisRunRepository.findByIds(analysisRunIds);
        analysisRuns.forEach(analysisRun -> {
            try {
                var project = analysisRun.getProject();
                var startDateTime = analysisRun.getStartDateTime();
                var endDateTime = analysisRun.getEndDateTime();

                analysisRunService.updateProgress(analysisRun,"Importing members",AnalysisRun.Progress.AtStartOfImportingMembers.getValue(), true);
                gitManagementUserService.saveGitManagementUserInfo(project);

                analysisRunService.updateProgress(analysisRun,"Importing merge requests for "+project.getNameWithNamespace(),AnalysisRun.Progress.AtStartOfImportingMergeRequests.getValue(), true);
                mergeRequestService.saveMergeRequestInfo(analysisRun, project, startDateTime, endDateTime);

                analysisRunService.updateProgress(analysisRun,"Importing commits for "+project.getNameWithNamespace(),AnalysisRun.Progress.AtStartOfImportingCommits.getValue(), true);
                commitService.saveCommitInfo(analysisRun, project, startDateTime, endDateTime);

                analysisRunService.updateProgress(analysisRun,"Importing issues for "+project.getNameWithNamespace(),AnalysisRun.Progress.AtStartOfImportingIssues.getValue(), true);
                issueService.saveIssueInfo(analysisRun, project, startDateTime, endDateTime);
                projectIds.add(project.getId());

                analysisRun.setStatus(AnalysisRun.Status.Completed);
                analysisRunService.updateProgress(analysisRun,"Analysis done for "+project.getNameWithNamespace(), AnalysisRun.Progress.Done.getValue(), true);
            } catch(Exception e) {
                System.out.println("yo" + e);
                analysisRun.setStatus(AnalysisRun.Status.Error);
                analysisRunService.updateProgress(analysisRun,"Error",0.0, true);
            }
        });
        return projectIds;
    }
}
