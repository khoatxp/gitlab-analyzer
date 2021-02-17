package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.*;
import com.eris.gitlabanalyzer.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final GitLabService gitLabService;

    public ProjectService(ProjectRepository projectRepository, GitLabService gitLabService) {
        this.projectRepository = projectRepository;
        this.gitLabService = gitLabService;
    }

    public Project saveProjectInfo(Long projectId) {
        var gitLabProject = gitLabService.getProject(projectId).block();
        // TODO Check if project already exists
        Project project = new Project(
                projectId,
                gitLabProject.getName(),
                gitLabProject.getNameWithNamespace(),
                gitLabProject.getWebUrl()
        );

        return projectRepository.save(project);
    }

    public RawTimeLineProjectData getTimeLineProjectData(Long gitLabProjectId, ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
        var mergeRequests = gitLabService.getMergeRequests(gitLabProjectId, startDateTime, endDateTime);

        // for all items in mergeRequests call get commits
            // for all items in commits call get diff
            // for all items in merge request get diff
        var rawMergeRequestData = mergeRequests
                .parallel()
                .runOn(Schedulers.boundedElastic())
                .map((mergeRequest) -> getRawMergeRequestData(mergeRequest, gitLabProjectId))
                .sorted((mr1, mr2) -> (int)(mr1.getGitLabMergeRequest().getIid() - mr2.getGitLabMergeRequest().getIid()));


        // for all commits NOT in merge commits get diff
        var mergeRequestCommitIds = getMergeRequestCommitIds(rawMergeRequestData);
        var commits = gitLabService.getCommits(gitLabProjectId, startDateTime, endDateTime);
        var orphanCommits = getOrphanCommits(commits, mergeRequestCommitIds);
        var rawOrphanCommitData = orphanCommits
                .parallel()
                .runOn(Schedulers.boundedElastic())
                .map((commit) -> getRawCommitData(commit, gitLabProjectId))
                .sorted(Comparator.comparing(c -> c.getGitLabCommit().getCreatedAt()));

        var rawProjectData = new RawTimeLineProjectData(gitLabProjectId, startDateTime, endDateTime, rawMergeRequestData, rawOrphanCommitData);

        return rawProjectData;
    }


    private RawMergeRequestData getRawMergeRequestData(GitLabMergeRequest mergeRequest, Long gitLabProjectId) {
        var gitLabCommits = gitLabService.getMergeRequestCommits(gitLabProjectId, mergeRequest.getIid());
        var rawCommitData = gitLabCommits
                .parallel()
                .runOn(Schedulers.boundedElastic())
                .map((commit) -> getRawCommitData(commit, gitLabProjectId))
                .sorted(Comparator.comparing(c -> c.getGitLabCommit().getCreatedAt()));

        var gitLabDiff = gitLabService.getMergeRequestDiff(gitLabProjectId, mergeRequest.getIid());

        var rawMergeRequestData = new RawMergeRequestData(rawCommitData, gitLabDiff, mergeRequest);
        return rawMergeRequestData;
    }

    private RawCommitData getRawCommitData(GitLabCommit commit, Long gitLabProjectId) {
        var changes = gitLabService.getCommitDiff(gitLabProjectId, commit.getSha());
        var rawCommitData = new RawCommitData(commit, changes);
        return rawCommitData;
    }

    private Mono<Set<String>> getMergeRequestCommitIds(Flux<RawMergeRequestData> mergeRequestData) {
        return mergeRequestData.flatMap(mergeRequest -> mergeRequest.getFluxRawCommitData())
                .map(commit -> commit.getFluxGitLabCommit().getSha())
                .collect(Collectors.toSet());
    }

    private Flux<GitLabCommit> getOrphanCommits(Flux<GitLabCommit> commits, Mono<Set<String>> mrCommitIds) {
        return mrCommitIds.flatMapMany(commitIds -> commits.filter(gitLabCommit -> !commitIds.contains(gitLabCommit.getSha())));
    }

    public List<Project> getProjects() {
        return projectRepository.findAll();
    }
}
