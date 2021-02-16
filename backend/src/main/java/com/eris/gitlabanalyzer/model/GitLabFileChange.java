package com.eris.gitlabanalyzer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

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

    public GitLabFileChange() {
    }

    public String getDiff() {
        return diff;
    }

    public String getNewPath() {
        return newPath;
    }

    public String getOldPath() {
        return oldPath;
    }

    public Boolean getNewFile() {
        return newFile;
    }

    public Boolean getRenamedFile() {
        return renamedFile;
    }

    public Boolean getDeletedFile() {
        return deletedFile;
    }
}
