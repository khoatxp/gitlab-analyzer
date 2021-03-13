package com.eris.gitlabanalyzer.dataprocessing;

public class CommentCharacter {
    private String singleLineComment;
    private String blockCommentStart;
    private String blockCommentEnd;


    public CommentCharacter(String singleLineComment, String blockCommentStart, String blockCommentEnd){
        this.singleLineComment = singleLineComment;
        this.blockCommentStart = blockCommentStart;
        this.blockCommentEnd = blockCommentEnd;
    }

    public String getSingleLineComment() {
        return singleLineComment;
    }

    public String getBlockCommentStart() {
        return blockCommentStart;
    }

    public String getBlockCommentEnd() {
        return blockCommentEnd;
    }
}
