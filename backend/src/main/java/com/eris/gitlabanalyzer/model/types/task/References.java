package com.eris.gitlabanalyzer.model.types.task;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Getter
public class References {
    @JsonProperty("short")
    private String shortRef;
    private String relative;
    private String full;
}