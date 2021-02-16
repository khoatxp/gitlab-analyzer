package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.*;
import com.eris.gitlabanalyzer.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
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

    public RawTimeLineProjectData getTimeLineProjectData(Long projectId, ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
        var mergeRequests = gitLabService.getMergeRequests(projectId, startDateTime, endDateTime);

        // for all items in mergeRequests call get commits
            // for all items in commits call get diff
            // for all items in merge request get diff
        var rawMergeRequestData = mergeRequests.map((mergeRequest) -> getRawMergeRequestData(mergeRequest, projectId));


        // for all commits NOT in merge commits get diff
        var mergeRequestCommitIds = getMergeRequestCommitIds(rawMergeRequestData);
        var commits = gitLabService.getCommits(projectId, startDateTime, endDateTime);
        var orphanCommits = getOrphanCommits(commits, mergeRequestCommitIds);
        var rawOrphanCommitData = orphanCommits.map((commit) -> getRawCommitData(commit, projectId));

        var rawProjectData = new RawTimeLineProjectData();
        rawProjectData.setProjectId(projectId);
        rawProjectData.setStartDateTime(startDateTime);
        rawProjectData.setEndDateTime(endDateTime);
        rawProjectData.setMergeRequestData(rawMergeRequestData);
        rawProjectData.setOrphanCommits(rawOrphanCommitData);

        return rawProjectData;
    }


    private RawMergeRequestData getRawMergeRequestData(GitLabMergeRequest mergeRequest, Long projectId) {
        var gitLabCommits = gitLabService.getMergeRequestCommits(projectId, mergeRequest.getIid());
        var rawCommitData = gitLabCommits.map((commit) -> getRawCommitData(commit, projectId));

        var gitLabDiff = gitLabService.getMergeRequestDiff(projectId, mergeRequest.getIid());

        var rawMergeRequestData = new RawMergeRequestData();
        rawMergeRequestData.setGitLabMergeRequest(mergeRequest);
        rawMergeRequestData.setRawCommitData(rawCommitData);
        rawMergeRequestData.setGitLabDiff(gitLabDiff);
        return rawMergeRequestData;
    }

    private RawCommitData getRawCommitData(GitLabCommit commit, Long projectId) {
        var changes = gitLabService.getCommitDiff(projectId, commit.getSha());
        var rawCommitData = new RawCommitData();
        rawCommitData.setGitLabCommit(commit);
        rawCommitData.setGitLabDiff(changes);
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
