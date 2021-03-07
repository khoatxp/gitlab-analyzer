package com.eris.gitlabanalyzer.model.gitlabresponse;
import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Getter
public class GitLabProject {
    private Long id;
    private String name;
    @JsonProperty("name_with_namespace")
    private String nameWithNamespace;
    @JsonProperty("web_url")
    private String webUrl;
}
