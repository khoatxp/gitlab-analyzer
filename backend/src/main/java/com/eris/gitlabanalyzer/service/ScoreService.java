package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.dataprocessing.CalculateDiffMetrics;
import com.eris.gitlabanalyzer.dataprocessing.DiffScoreCalculator;
import com.eris.gitlabanalyzer.model.Commit;
import com.eris.gitlabanalyzer.model.MergeRequest;
import com.eris.gitlabanalyzer.repository.CommitRepository;
import com.eris.gitlabanalyzer.repository.MergeRequestRepository;
import com.eris.gitlabanalyzer.viewmodel.ScoreDigest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    public double getMergeDiffScore(Long mergeRequestId, Long scoreProfileId) {
        return round(diffScoreCalculator.calculateScoreMerge(mergeRequestId, scoreProfileId));
    }

    public double[] getUserSingleMergeScore(Long gitManagementUserId, Long mergeId, Long scoreProfileId) {
        Optional<MergeRequest> mr = mergeRequestRepository.findById(mergeId);
        double sharedMergeScoreTotal = 0;
        double mergeScoreTotal = 0;
        if (mr.isEmpty()) {
            return null;
        }
        boolean isShared = !mr.get().getSharedWith().isEmpty();
        // if shared sums users commit score on MR else finds mergeScore
        if (isShared) {
            List<Commit> commitsOnSharedMr = commitRepository.findByMergeIdAndGitManagementUserId(mergeId, gitManagementUserId);
            for (Commit commit : commitsOnSharedMr) {
                sharedMergeScoreTotal = diffScoreCalculator.calculateScoreCommit(commit.getId(), scoreProfileId);
            }
        } else {
            mergeScoreTotal = diffScoreCalculator.calculateScoreMerge(mergeId, scoreProfileId);
        }
        return new double[]{round(mergeScoreTotal), round(sharedMergeScoreTotal)};
    }

    public double[] getUserMergeScore(Long gitManagementUserId, Long projectId, Long scoreProfileId, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        List<MergeRequest> mergeRequests = mergeRequestRepository.findAllActiveNotSharedByGitManagementUserIdAndDateRange(projectId, gitManagementUserId, startDateTime, endDateTime);
        // Find shared MR for user that they both own or participated on
        List<MergeRequest> sharedMergeRequests = mergeRequestRepository.findActiveOwnerSharedMergeRequests(projectId, gitManagementUserId, startDateTime, endDateTime);
        sharedMergeRequests.addAll(mergeRequestRepository.findActiveParticipantSharedMergeRequests(projectId, gitManagementUserId, startDateTime, endDateTime));

        double mergeScoreTotal = 0;

        for (MergeRequest mr : mergeRequests) {
            mergeScoreTotal += diffScoreCalculator.calculateScoreMerge(mr.getId(), scoreProfileId);
        }

        List<Commit> commitsOnSharedMr = new ArrayList<>();
        for (MergeRequest mr : sharedMergeRequests) {
            commitsOnSharedMr.addAll(commitRepository.findActiveByMergeIdAndGitManagementUserId(mr.getId(), gitManagementUserId));
        }
        double sharedMergeScoreTotal = 0;
        for (Commit commit : commitsOnSharedMr) {
            sharedMergeScoreTotal += diffScoreCalculator.calculateScoreCommit(commit.getId(), scoreProfileId);
        }

        return new double[]{round(mergeScoreTotal), round(sharedMergeScoreTotal)};
    }

    public void saveMergeDiffMetrics(MergeRequest mergeRequest) {
        calculateDiffMetrics.storeMetricsMerge(mergeRequest);
    }

    public double getTotalMergeDiffScore(Long projectId, Long scoreProfileId, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        List<MergeRequest> mergeRequests = mergeRequestRepository.findAllActiveByProjectIdAndDateRange(projectId,
                startDateTime.withOffsetSameInstant(ZoneOffset.UTC), endDateTime.withOffsetSameInstant(ZoneOffset.UTC));
        double totalScore = 0;
        for (MergeRequest mr : mergeRequests) {
            totalScore += diffScoreCalculator.calculateScoreMerge(mr.getId(), scoreProfileId);
        }

        return round(totalScore);
    }

    public MergeRequest toggleIgnoreMergeFromScore(Long mergeId) {
        MergeRequest mergeRequest = this.mergeRequestRepository
                .findById(mergeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Merge request of not found with id: " + mergeId));

        mergeRequest.setIsIgnored(!mergeRequest.getIsIgnored());

        this.mergeRequestRepository.save(mergeRequest);
        this.commitRepository.updateCommitIsIgnoredByMergeRequestId(mergeRequest.getIsIgnored(), mergeRequest.getId());
        return mergeRequest;
    }

    // This will most likely change as we update how we retrieve diff's
    public double getCommitDiffScore(Long commitId, Long scoreProfileId) {
        return round(diffScoreCalculator.calculateScoreCommit(commitId, scoreProfileId));
    }

    public double getUserCommitScore(Long projectId, Long gitManagementUserId, Long scoreProfileId, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {

        List<Commit> commits = commitRepository.findAllActiveByProjectIdAndDateRangeAndGitManagementUserId(projectId, gitManagementUserId,
                startDateTime.withOffsetSameInstant(ZoneOffset.UTC), endDateTime.withOffsetSameInstant(ZoneOffset.UTC));
        double totalScore = 0;
        for (Commit commit : commits) {
            totalScore += diffScoreCalculator.calculateScoreCommit(commit.getId(), scoreProfileId);
        }
        return round(totalScore);
    }

    public void saveCommitDiffMetrics(Commit commit) {
        calculateDiffMetrics.storeMetricsCommit(commit);
    }


    public double getTotalCommitDiffScore(Long projectId, Long scoreProfileId, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        List<Commit> commits = commitRepository.findAllActiveByProjectIdAndDateRange(projectId,
                startDateTime.withOffsetSameInstant(ZoneOffset.UTC), endDateTime.withOffsetSameInstant(ZoneOffset.UTC));
        double totalScore = 0;
        for (Commit commit : commits) {
            totalScore += diffScoreCalculator.calculateScoreCommit(commit.getId(), scoreProfileId);
        }
        return round(totalScore);
    }

    public Commit toggleIgnoreCommitFromScore(Long commitId) {
        Commit commit = this.commitRepository
                .findById(commitId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Commit not found with id: " + commitId));

        commit.setIsIgnored(!commit.getIsIgnored());
        this.commitRepository.save(commit);
        return commit;
    }

    public List<ScoreDigest> getDailyScoreDigest(Long projectId, Long gitManagementUserId, Long scoreProfileId, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {

        var startDateTimeUTC = startDateTime.withOffsetSameInstant(ZoneOffset.UTC);
        var endDateTimeUTC = endDateTime.withOffsetSameInstant(ZoneOffset.UTC);
        List<Commit> commits = gitManagementUserId != 0L ? commitRepository.findAllActiveByProjectIdAndDateRangeAndGitManagementUserId(projectId, gitManagementUserId, startDateTime, endDateTime)
                : commitRepository.findAllActiveByProjectIdAndDateRange(projectId, startDateTimeUTC, endDateTimeUTC);

        List<MergeRequest> mergeRequests = gitManagementUserId != 0L ? mergeRequestRepository.findAllActiveByGitManagementUserIdAndDateRange(projectId, gitManagementUserId, startDateTime, endDateTime)
                : mergeRequestRepository.findAllActiveByProjectIdAndDateRange(projectId, startDateTimeUTC, endDateTimeUTC);

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
            double totalDayMergeScore = 0;
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
                    totalDayMergeScore += diffScoreCalculator.calculateScoreMerge(mergeRequest.getId(), scoreProfileId);
                }
            }
            return new ScoreDigest(round(totalDayCommitScore), round(totalDayMergeScore), commitCount, mergeCount, commitDate);
        }).collect(Collectors.toList());

        return digests;
    }

    private double round(double value){
        return Math.round( value *100.0 )/100.0;
    }
}
