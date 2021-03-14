package com.eris.gitlabanalyzer.dataprocessing;

import lombok.Data;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Data
public class CommentCharacter {
    private String singleLineComment;
    private String blockCommentStart;
    private String blockCommentEnd;
}
