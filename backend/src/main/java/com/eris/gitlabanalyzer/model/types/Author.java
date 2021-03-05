package com.eris.gitlabanalyzer.model.types;

import com.fasterxml.jackson.annotation.*;

@lombok.Getter
public class Author {
    private long id;
    private String name;
    private String username;
    @JsonProperty("avatar_url")
    private String avatarURL;
    @JsonProperty("web_url")
    private String webURL;
    @JsonProperty("created_at")
    private String createdAt;
}
