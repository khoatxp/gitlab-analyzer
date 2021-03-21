package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.dataprocessing.CalculateDiffMetrics;
import com.eris.gitlabanalyzer.dataprocessing.DiffScoreCalculator;
import com.eris.gitlabanalyzer.model.Commit;
import com.eris.gitlabanalyzer.model.MergeRequest;
import com.eris.gitlabanalyzer.repository.CommitRepository;
import com.eris.gitlabanalyzer.repository.MergeRequestRepository;
import com.eris.gitlabanalyzer.viewmodel.ScoreDigest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class ScoreService {

    private final DiffScoreCalculator diffScoreCalculator;
    private final CalculateDiffMetrics calculateDiffMetrics;
    private final MergeRequestRepository mergeRequestRepository;
    private final CommitRepository commitRepository;

    @Autowired
    public ScoreService(DiffScoreCalculator diffScoreCalculator,
                        CalculateDiffMetrics calculateDiffMetrics, MergeRequestRepository mergeRequestRepository,
                        CommitRepository commitRepository) {
        this.diffScoreCalculator = diffScoreCalculator;
        this.calculateDiffMetrics = calculateDiffMetrics;
        this.mergeRequestRepository = mergeRequestRepository;
        this.commitRepository = commitRepository;
    }

    // This will most likely change as we update how we retrieve diff's
    public double getMergeDiffScore( Long mergeRequestId, Long scoreProfileId){
        return diffScoreCalculator.calculateScoreMerge(mergeRequestId, scoreProfileId);
    }

    public void saveMergeDiffMetrics(MergeRequest mergeRequest){
        calculateDiffMetrics.storeMetricsMerge(mergeRequest);
    }

    public double getTotalMergeDiffScore(Long projectId, Long scoreProfileId, OffsetDateTime startDateTime, OffsetDateTime endDateTime){
        List<MergeRequest> mergeRequests = mergeRequestRepository.findAllByProjectIdAndDateRange(projectId,
                startDateTime.withOffsetSameInstant(ZoneOffset.UTC), endDateTime.withOffsetSameInstant(ZoneOffset.UTC));
        double totalScore = 0;
        for( MergeRequest mr : mergeRequests){
            totalScore += diffScoreCalculator.calculateScoreMerge(mr.getId(), scoreProfileId);
        }

        return totalScore;
    }

    // This will most likely change as we update how we retrieve diff's
    public double getCommitDiffScore(Long commitId, Long scoreProfileId){

        return diffScoreCalculator.calculateScoreCommit(commitId, scoreProfileId);
    }

    public void saveCommitDiffMetrics(Commit commit){
        calculateDiffMetrics.storeMetricsCommit(commit);
    }


    // This will most likely change as we update how we retrieve diff's
    public double getTotalCommitDiffScore(Long projectId, Long scoreProfileId, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        List<Commit> commits = commitRepository.findAllByProjectIdAndDateRange(projectId,
                startDateTime.withOffsetSameInstant(ZoneOffset.UTC), endDateTime.withOffsetSameInstant(ZoneOffset.UTC));
        double totalScore = 0;
        for (Commit commit : commits) {
            totalScore += diffScoreCalculator.calculateScoreCommit(commit.getId(), scoreProfileId);
        }
        return totalScore;
    }

    public List<ScoreDigest> getDailyScoreDigest(Long projectId, Long scoreProfileId, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {

        var startDateTimeUTC = startDateTime.withOffsetSameInstant(ZoneOffset.UTC);
        var endDateTimeUTC = endDateTime.withOffsetSameInstant(ZoneOffset.UTC);
        List<Commit> commits = commitRepository.findAllByProjectIdAndDateRange(projectId, startDateTimeUTC, endDateTimeUTC);
        List<MergeRequest> mergeRequests = mergeRequestRepository.findAllByProjectIdAndDateRange(projectId, startDateTimeUTC, endDateTimeUTC);

        var groupedCommits = commits.stream()
                .collect(groupingBy((commit) ->
                        commit.getCreatedAt()
                                .withOffsetSameInstant(startDateTime.getOffset())
                                .toLocalDate()
                ));
        var groupedMergeRequest = mergeRequests.stream()
                .collect(groupingBy((mergeRequest) ->
                        mergeRequest.getMergedAt()
                                .withOffsetSameInstant(startDateTime.getOffset())
                                .toLocalDate()
                ));

        // If there is a commit with created_at earlier than startDateTime, use that date as range startDate
        var startDate = startDateTime.toLocalDate();
        for (var commitDate : groupedCommits.keySet()) {
            if (commitDate.isBefore(startDate)) {
                startDate = commitDate;
            }
        }

        var endDate = endDateTime.toLocalDate().plusDays(1); // datesUntil is inclusive/exclusive

        var digests = startDate.datesUntil(endDate).map(commitDate -> {
            double totalDayCommitScore = 0;
            double totalMergeScore = 0;
            int commitCount = 0;
            int mergeCount = 0;
            if (groupedCommits.containsKey(commitDate)) {
                var dailyCommits = groupedCommits.get(commitDate);
                commitCount = dailyCommits.size();
                for (var commit : dailyCommits) {
                    totalDayCommitScore += diffScoreCalculator.calculateScoreCommit(commit.getId(), scoreProfileId);
                }
            }
            if (groupedMergeRequest.containsKey(commitDate)) {
                var dailyMerges = groupedMergeRequest.get(commitDate);
                mergeCount = dailyMerges.size();
                for (var mergeRequest : dailyMerges) {
                    totalMergeScore += diffScoreCalculator.calculateScoreMerge(mergeRequest.getId(), scoreProfileId);
                }
            }
            return new ScoreDigest(totalDayCommitScore, totalMergeScore, commitCount, mergeCount, commitDate);
        }).collect(Collectors.toList());

        return digests;
    }
}
