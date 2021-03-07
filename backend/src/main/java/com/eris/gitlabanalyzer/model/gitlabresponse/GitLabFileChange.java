package com.eris.gitlabanalyzer.model.gitlabresponse;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Getter
public class GitLabFileChange {
    private String diff;
    @JsonProperty("new_path")
    private String newPath;
    @JsonProperty("old_path")
    private String oldPath;
    @JsonProperty("new_file")
    private Boolean newFile;
    @JsonProperty("renamed_file")
    private Boolean renamedFile;
    @JsonProperty("deleted_file")
    private Boolean deletedFile;
}
