package com.eris.gitlabanalyzer.model;
import reactor.core.publisher.Flux;

import java.time.ZonedDateTime;
import java.util.List;

public class RawTimeLineProjectData {

    private Long projectId;
    private ZonedDateTime startDateTime;
    private ZonedDateTime endDateTime;

    Flux<RawMergeRequestData> mergeRequestData;
    Flux<RawCommitData> orphanCommits;

    public RawTimeLineProjectData() {
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public ZonedDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(ZonedDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public ZonedDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(ZonedDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public List<RawMergeRequestData> getMergeRequestData() {
        return mergeRequestData.collectList().block();
    }

    public void setMergeRequestData(Flux<RawMergeRequestData> mergeRequestData) {
        this.mergeRequestData = mergeRequestData;
    }

    public List<RawCommitData> getOrphanCommits() {
        if (orphanCommits != null) {
            return orphanCommits.collectList().block();
        }
        return null;
    }

    public void setOrphanCommits(Flux<RawCommitData> orphanCommits) {
        this.orphanCommits = orphanCommits;
    }
}

