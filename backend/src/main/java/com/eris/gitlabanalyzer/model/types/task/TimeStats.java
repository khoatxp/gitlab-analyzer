
package com.eris.gitlabanalyzer.model.types.task;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class TimeStats {
    @JsonProperty("time_estimate")
    private int timeEstimate;
    @JsonProperty("total_time_spent")
    private int totalTimeSpent;
    @JsonProperty("human_time_estimate")
    private String humanTimeEstimate;
    @JsonProperty("human_total_time_spent")
    private String humanTotalTimeSpent;
}