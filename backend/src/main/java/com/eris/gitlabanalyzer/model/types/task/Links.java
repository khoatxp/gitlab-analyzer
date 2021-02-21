package com.eris.gitlabanalyzer.model.types.task;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Getter
public class Links {
    private String self;
    private String notes;
    @JsonProperty("award_emoji")
    private String awardEmoji;
    private String project;
}