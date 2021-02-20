package com.eris.gitlabanalyzer.model.types.note;

import com.fasterxml.jackson.annotation.*;

@lombok.Data
public class Position {
    @JsonProperty("base_sha")
    private String baseSha;
    @JsonProperty("start_sha")
    private String startSha;
    @JsonProperty("head_sha")
    private String headSha;
    @JsonProperty("old_path")
    private String oldPath;
    @JsonProperty("new_path")
    private String newPath;
    @JsonProperty("position_type")
    private String positionType;
    @JsonProperty("old_line")
    private Long oldLine;
    @JsonProperty("new_line")
    private Long newLine;
    @JsonProperty("line_range")
    private LineRange lineRange;
}
