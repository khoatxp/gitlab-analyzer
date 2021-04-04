package com.eris.gitlabanalyzer.viewmodel;

import com.eris.gitlabanalyzer.model.Commit;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class CommitView {
    Long id;
    String sha;
    String author_email;
    String author_name;
    OffsetDateTime created_at;
    String title;
    String web_url;

    public static CommitView fromCommit(Commit commit) {
        return new CommitView(
                commit.getId(),
                commit.getSha(),
                commit.getAuthorEmail(),
                commit.getAuthorName(),
                commit.getCreatedAt(),
                commit.getTitle(),
                commit.getWebUrl()
        );
    }
}
