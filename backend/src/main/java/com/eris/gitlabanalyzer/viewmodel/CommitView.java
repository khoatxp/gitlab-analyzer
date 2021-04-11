package com.eris.gitlabanalyzer.viewmodel;

import com.eris.gitlabanalyzer.model.Commit;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class CommitView {
    private final Long id;
    private final String sha;
    private final String title;
    private final String webUrl;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime mergedAt;
    private final String authorEmail;
    private final String authorName;

    public static CommitView fromCommit(Commit commit){
        return new CommitView(
                commit.getId(),
                commit.getSha(),
                commit.getTitle(),
                commit.getWebUrl(),
                commit.getCreatedAt(),
                commit.getMergedAt(),
                commit.getAuthorEmail(),
                commit.getAuthorName()
        );
    }
}
