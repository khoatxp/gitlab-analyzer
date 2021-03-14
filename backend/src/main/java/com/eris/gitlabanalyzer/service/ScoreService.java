package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.dataprocessing.CalculateDiffMetrics;
import com.eris.gitlabanalyzer.dataprocessing.DiffScoreCalculator;
import com.eris.gitlabanalyzer.model.Commit;
import com.eris.gitlabanalyzer.model.MergeRequest;
import com.eris.gitlabanalyzer.model.gitlabresponse.GitLabCommit;
import com.eris.gitlabanalyzer.model.gitlabresponse.GitLabMergeRequest;
import com.eris.gitlabanalyzer.repository.CommitRepository;
import com.eris.gitlabanalyzer.repository.MergeRequestRepository;
import com.eris.gitlabanalyzer.viewmodel.ScoreDigest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScoreService {

    private GitLabService gitLabService;
    private final DiffScoreCalculator diffScoreCalculator;
    private final CalculateDiffMetrics calculateDiffMetrics;
    private final MergeRequestRepository mergeRequestRepository;
    private final CommitRepository commitRepository;
    private final String serverUrl;
    private final String accessToken;

    @Autowired
    public ScoreService(DiffScoreCalculator diffScoreCalculator,
                        CalculateDiffMetrics calculateDiffMetrics, MergeRequestRepository mergeRequestRepository,
                        CommitRepository commitRepository,
                        @Value("${gitlab.SERVER_URL}") String serverUrl, // TODO Remove after server info is correctly retrieved based on internal projectId
                        @Value("${gitlab.ACCESS_TOKEN}") String accessToken) {
        this.diffScoreCalculator = diffScoreCalculator;
        this.calculateDiffMetrics = calculateDiffMetrics;
        this.mergeRequestRepository = mergeRequestRepository;
        this.commitRepository = commitRepository;
        this.serverUrl = serverUrl;
        this.accessToken = accessToken;
        gitLabService = new GitLabService(this.serverUrl, this.accessToken);
    }

    // This will most likely change as we update how we retrieve diff's
    public int getMergeDiffScore(Long projectId, Long mergeRequestId) {
        calculateDiffMetrics.storeMetricsMerge(projectId, mergeRequestId);
        return diffScoreCalculator.calculateScoreMerge(mergeRequestId);
    }

    // This will most likely change as we update how we retrieve diff's
    public int getTotalMergeDiffScore(Long projectId, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        Iterable<GitLabMergeRequest> mergeRequests = gitLabService.getMergeRequests(projectId, startDateTime, endDateTime).toIterable();
        int totalScore = 0;
        for (GitLabMergeRequest gitLabMergeRequest : mergeRequests) {
            MergeRequest mr = mergeRequestRepository.findByIidAndProjectId(gitLabMergeRequest.getIid(), projectId);
            calculateDiffMetrics.storeMetricsMerge(projectId, mr.getId());
            totalScore += diffScoreCalculator.calculateScoreMerge(mr.getId());
        }

        return totalScore;
    }

    // This will most likely change as we update how we retrieve diff's
    public int getCommitDiffScore(Long projectId, Long commitId) {
        calculateDiffMetrics.storeMetricsCommit(commitId, projectId);
        return diffScoreCalculator.calculateScoreCommit(commitId);
    }

    // This will most likely change as we update how we retrieve diff's
    public int getTotalCommitDiffScore(Long projectId, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        Iterable<GitLabCommit> commits = gitLabService.getCommits(projectId, startDateTime, endDateTime).toIterable();
        int totalScore = 0;
        for (GitLabCommit gitLabCommit : commits) {
            Commit commit = commitRepository.findByCommitShaAndProjectId(gitLabCommit.getSha(), projectId);
            calculateDiffMetrics.storeMetricsMerge(projectId, commit.getId());
            totalScore += diffScoreCalculator.calculateScoreMerge(commit.getId());
        }
        return totalScore;
    }

    public List<ScoreDigest> getDailyScoreDigest(Long projectId, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        var digests = new ArrayList<ScoreDigest>();

        var start = startDateTime;
        var end = startDateTime.truncatedTo(ChronoUnit.DAYS).plusDays(1);

        while (start.isBefore(endDateTime)) {

            if (end.isAfter(endDateTime)) {
                end = endDateTime;
            }

            var utcStart = start.withOffsetSameInstant(ZoneOffset.UTC);
            var utcEnd = end.withOffsetSameInstant(ZoneOffset.UTC);

            // Get total score for one day
            var commitScore = getTotalCommitDiffScore(projectId, utcStart, utcEnd);
            var mergeScore = getTotalMergeDiffScore(projectId, utcStart, utcEnd);

            digests.add(new ScoreDigest(Double.valueOf(commitScore), Double.valueOf(mergeScore), start.toLocalDate()));
            start = end;
            end = end.plusDays(1);
        }

        return digests;
    }

}
