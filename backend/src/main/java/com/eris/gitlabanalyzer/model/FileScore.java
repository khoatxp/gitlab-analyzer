package com.eris.gitlabanalyzer.model;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "FileScore")
@Table(name = "file_score")
@Data
@NoArgsConstructor
public class FileScore {
    @Id
    @SequenceGenerator(
            name = "file_score_sequence",
            sequenceName = "file_score_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "file_score_sequence"
    )
    @Column(
            name = "file_score_id"
    )
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "commit_id",
            referencedColumnName = "commit_id")
    private Commit commit;

    @ManyToOne
    @JoinColumn(
            name = "merge_request_id",
            referencedColumnName = "merge_request_id")
    private MergeRequest mergeRequest;

    @Column(
            name = "file_type"
    )
    private String fileType;

    @Column(
            name = "file_path"
    )
    private String filePath;

    @Column(
            name = "code_line_added"
    )
    private int codeLineAdded;

    @Column(
            name = "syntax_line_added"
    )
    private int syntaxLineAdded;

    @Column(
            name = "comment_line_added"
    )
    private int commentLineAdded;

    @Column(
            name = "code_line_remove"
    )
    private int codeLineRemoved;

    @Column(
            name = "syntax_line_remove"
    )
    private int syntaxLineRemoved;

    @Column(
            name = "comment_line_remove"
    )
    private int commentLineRemoved;

    public FileScore(MergeRequest mergeRequest, String fileType, String filePath,
                     int codeLineAdded, int syntaxLineAdded, int commentLineAdded, int codeLineRemoved,
                     int syntaxLineRemoved, int commentLineRemoved){
        this.mergeRequest = mergeRequest;
        this.fileType = fileType;
        this.filePath = filePath;
        this.codeLineAdded = codeLineAdded;
        this.syntaxLineAdded = syntaxLineAdded;
        this.commentLineAdded = commentLineAdded;
        this.codeLineRemoved = codeLineRemoved;
        this.syntaxLineRemoved = syntaxLineRemoved;
        this. commentLineRemoved = commentLineRemoved;
    }

    public FileScore(Commit commit, String fileType, String filePath,
                     int codeLineAdded, int syntaxLineAdded, int commentLineAdded, int codeLineRemoved,
                     int syntaxLineRemoved, int commentLineRemoved){
        this.commit = commit;
        this.fileType = fileType;
        this.filePath = filePath;
        this.codeLineAdded = codeLineAdded;
        this.syntaxLineAdded = syntaxLineAdded;
        this.commentLineAdded = commentLineAdded;
        this.codeLineRemoved = codeLineRemoved;
        this.syntaxLineRemoved = syntaxLineRemoved;
        this.commentLineRemoved = commentLineRemoved;
    }

}
