package com.eris.gitlabanalyzer.model.gitlabresponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GitLabMergeRequest {
    private Long id;
    private Long iid;
    private String title;
    private String description;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("web_url")
    private String webUrl;

    public GitLabMergeRequest(){}

    public Long getId() {
        return id;
    }

    public Long getIid() {
        return iid;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getWebUrl() {
        return webUrl;
    }

    @Override
    public String toString() {
        return "GitLabMergeRequest{" +
                "id=" + id +
                ", iid=" + iid +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", webUrl='" + webUrl + '\'' +
                '}';
    }
}
