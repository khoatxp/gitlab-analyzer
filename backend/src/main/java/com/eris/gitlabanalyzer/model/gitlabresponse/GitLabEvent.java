package com.eris.gitlabanalyzer.model.gitlabresponse;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Getter
public class GitLabEvent {
    @JsonProperty("author")
    GitLabMember member;
}
