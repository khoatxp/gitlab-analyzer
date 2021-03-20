package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.model.User;
import com.eris.gitlabanalyzer.model.UserServer;
import com.eris.gitlabanalyzer.viewmodel.ProgressView;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
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

    public AnalyticsService(ProjectService projectService, GitManagementUserService gitManagementUserService, MergeRequestService mergeRequestService, CommitService commitService, IssueService issueService, MessageService messageService) {
        this.projectService = projectService;
        this.gitManagementUserService = gitManagementUserService;
        this.mergeRequestService = mergeRequestService;
        this.commitService = commitService;
        this.issueService = issueService;
        this.messageService = messageService;
    }

    public List<Long> saveAllFromGitlab(User user, List<Long> gitLabProjectIdList, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        List<Long> projectIds = new ArrayList<>();
        gitLabProjectIdList.forEach(gitLabProjectId -> {
            messageService.sendMessage(new ProgressView("Importing general project information","10"), user);
            Project project = projectService.saveProjectInfo(user, gitLabProjectId);

            messageService.sendMessage(new ProgressView("Importing members for "+project.getNameWithNamespace(),"20"), user);
            gitManagementUserService.saveGitManagementUserInfo(project);

            messageService.sendMessage(new ProgressView("Importing merge requests for "+project.getNameWithNamespace(),"30"), user);
            mergeRequestService.saveMergeRequestInfo(project, startDateTime, endDateTime);

            messageService.sendMessage(new ProgressView("Importing commits for "+project.getNameWithNamespace(),"40"), user);
            commitService.saveCommitInfo(project, startDateTime, endDateTime);

            messageService.sendMessage(new ProgressView("Importing issues for "+project.getNameWithNamespace(),"80"), user);
            issueService.saveIssueInfo(project, startDateTime, endDateTime);

            messageService.sendMessage(new ProgressView("Analysis done for "+project.getNameWithNamespace(), "100"), user);
            projectIds.add(project.getId());
        });
        return projectIds;
    }
}
