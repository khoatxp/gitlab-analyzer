package com.eris.gitlabanalyzer.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GitLabProject {
    private Long id;

    private String name;

    @JsonProperty("name_with_namespace")
    private String nameWithNamespace;

    @JsonProperty("web_url")
    private String webUrl;
}
