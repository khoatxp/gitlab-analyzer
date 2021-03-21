package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.AnalysisRun;
import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.model.User;
import com.eris.gitlabanalyzer.repository.AnalysisRunRepository;
import com.eris.gitlabanalyzer.model.AnalyticsProgress;
import com.eris.gitlabanalyzer.repository.AnalyticsProgressRepository;
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
    private final MessageService messageService;
    private final AnalyticsProgressRepository analyticsProgressRepository;
    private final AnalysisRunService analysisRunService;
    private final AnalysisRunRepository analysisRunRepository;

    public AnalyticsService(ProjectService projectService,
                            GitManagementUserService gitManagementUserService,
                            MergeRequestService mergeRequestService,
                            CommitService commitService,
                            IssueService issueService,
                            MessageService messageService,
                            AnalyticsProgressRepository analyticsProgressRepository,
                            AnalysisRunService analysisRunService) {
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
        this.messageService = messageService;
        this.analyticsProgressRepository = analyticsProgressRepository;
        this.analysisRunService = analysisRunService;
        this.analysisRunRepository = analysisRunRepository;
    }

    public List<Long> saveAllFromGitlab(User user, List<Long> gitLabProjectIdList, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        List<AnalysisRun> analysisRuns = saveProjectsAndAnalysisRuns(user, gitLabProjectIdList, startDateTime, endDateTime);
        return loadProjectDataForAnalysisRuns(analysisRuns);
    }

    public List<AnalysisRun> saveProjectsAndAnalysisRuns(User user, List<Long> gitLabProjectIdList, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        List<AnalysisRun> analysisRuns = new ArrayList<>();
        gitLabProjectIdList.forEach(gitLabProjectId -> {
            Project project = projectService.saveProjectInfo(user, gitLabProjectId);
            analysisRuns.add(this.analysisRunService.createAnalysisRun(user, project, AnalysisRun.Status.InProgress, startDateTime, endDateTime));
        });
        return analysisRuns;
    }

    public List<Long> loadProjectDataForAnalysisRuns(List<AnalysisRun> analysisRuns) {
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
                analysisRun.setStatus(AnalysisRun.Status.Error);
                analysisRunRepository.save(analysisRun);
            }
        });
        return projectIds;
    }

    public void updateProgress(AnalyticsProgress analyticsProgress, String message, String progress, User user){
        analyticsProgress.setMessage(message);
        analyticsProgress.setProgress(progress);
        messageService.sendMessage(analyticsProgressRepository.save(analyticsProgress), user);
    }

    public AnalyticsProgress getProgress(Long userId){
        return analyticsProgressRepository.findByUserId(userId);
    }
        public List<Long> saveAllFromGitlab(User user, List<Long> gitLabProjectIdList, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
            List<Long> projectIds = new ArrayList<>();
            gitLabProjectIdList.forEach(gitLabProjectId -> {
                AnalyticsProgress analyticsProgress = new AnalyticsProgress(startDateTime, endDateTime, user.getId());

                updateProgress(analyticsProgress,"Importing general project information","0", user);
                Project project = projectService.saveProjectInfo(user, gitLabProjectId);

                updateProgress(analyticsProgress,"Importing members for "+project.getNameWithNamespace(),"10", user);
                gitManagementUserService.saveGitManagementUserInfo(project);

                updateProgress(analyticsProgress,"Importing merge requests for "+project.getNameWithNamespace(),"20", user);
                mergeRequestService.saveMergeRequestInfo(project, startDateTime, endDateTime);

                updateProgress(analyticsProgress,"Importing commits for "+project.getNameWithNamespace(),"40", user);
                commitService.saveCommitInfo(project, startDateTime, endDateTime);

                updateProgress(analyticsProgress,"Importing issues for "+project.getNameWithNamespace(),"80", user);
                issueService.saveIssueInfo(project, startDateTime, endDateTime);

                analyticsProgress.setProjectId(project.getId());
                updateProgress(analyticsProgress,"Analysis done for "+project.getNameWithNamespace(), "100", user);

                analyticsProgressRepository.deleteById(analyticsProgress.getId());

                projectIds.add(project.getId());
                this.analysisRunService.createAnalysisRun(user, project, startDateTime, endDateTime);
            });
            return projectIds;
        }


}
