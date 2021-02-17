package com.eris.gitlabanalyzer.model;
import reactor.core.publisher.Flux;

import java.time.ZonedDateTime;
import java.util.List;

public class RawTimeLineProjectData {

    private Long gitLabProjectId;
    private ZonedDateTime startDateTime;
    private ZonedDateTime endDateTime;
    private Flux<RawMergeRequestData> mergeRequestData;
    private Flux<RawCommitData> orphanCommits;

    public RawTimeLineProjectData() {
    }

    public RawTimeLineProjectData(Long gitLabProjectId,
                                  ZonedDateTime startDateTime,
                                  ZonedDateTime endDateTime,
                                  Flux<RawMergeRequestData> mergeRequestData,
                                  Flux<RawCommitData> orphanCommits) {
        this.gitLabProjectId = gitLabProjectId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.mergeRequestData = mergeRequestData;
        this.orphanCommits = orphanCommits;
    }

    public Long getGitLabProjectId() {
        return gitLabProjectId;
    }

    public ZonedDateTime getStartDateTime() {
        return startDateTime;
    }

    public ZonedDateTime getEndDateTime() {
        return endDateTime;
    }

    public List<RawMergeRequestData> getMergeRequestData() {
        return mergeRequestData.collectList().block();
    }

    public List<RawCommitData> getOrphanCommits() {
        if (orphanCommits != null) {
            return orphanCommits.collectList().block();
        }
        return null;
    }
}

