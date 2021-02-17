package com.eris.gitlabanalyzer.model.types;

import com.fasterxml.jackson.annotation.*;

@lombok.Data
public class Author {
    private long id;
    private String name;
    private String username;
    private String state;
    @JsonProperty("avatar_url")
    private String avatarURL;
    @JsonProperty("web_url")
    private String webURL;
}
