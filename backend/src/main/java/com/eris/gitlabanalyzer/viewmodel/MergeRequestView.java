package com.eris.gitlabanalyzer.viewmodel;

import com.eris.gitlabanalyzer.model.MergeRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class MergeRequestView {
    private final Long id;
    private final Long iid;
    private final String authorUsername;
    private final String authorName;
    private final String title;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime mergedAt;
    private final String webUrl;
    private final boolean ignored;

    public static MergeRequestView fromMergeRequest(MergeRequest mergeRequest){
        return new MergeRequestView(
                mergeRequest.getId(),
                mergeRequest.getIid(),
                mergeRequest.getAuthorUsername(),
                mergeRequest.getGitManagementUser().getName(),
                mergeRequest.getTitle(),
                mergeRequest.getCreatedAt(),
                mergeRequest.getMergedAt(),
                mergeRequest.getWebUrl(),
                mergeRequest.getIsIgnored()
        );
    }
}
