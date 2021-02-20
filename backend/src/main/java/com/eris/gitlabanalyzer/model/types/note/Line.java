package com.eris.gitlabanalyzer.model.types.note;

import com.fasterxml.jackson.annotation.*;

@lombok.Data
public class Line {
    @JsonProperty("line_code")
    private String lineCode;
    private String type;
    @JsonProperty("old_line")
    private Long oldLine;
    @JsonProperty("new_line")
    private Long newLine;
}
