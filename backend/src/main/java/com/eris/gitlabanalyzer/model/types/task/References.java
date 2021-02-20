package com.eris.gitlabanalyzer.model.types.task;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class References {
    @JsonProperty("short")
    private String shortRef;
    private String relative;
    private String full;
}