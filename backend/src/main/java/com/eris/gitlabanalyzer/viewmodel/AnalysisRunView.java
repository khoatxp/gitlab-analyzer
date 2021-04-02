package com.eris.gitlabanalyzer.viewmodel;

import com.eris.gitlabanalyzer.model.AnalysisRun;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class AnalysisRunView {
    private final Long id;
    private final Long projectId;
    private final String projectNameWithNamespace;
    private final String projectName;
    private final String status;
    private final OffsetDateTime startDateTime;
    private final OffsetDateTime endDateTime;
    private final OffsetDateTime createdDateTime;

    public static AnalysisRunView fromAnalysisRun(AnalysisRun analysisRun) {
        return new AnalysisRunView(
                analysisRun.getId(),
                analysisRun.getProject().getId(),
                analysisRun.getProject().getNameWithNamespace(),
                analysisRun.getProject().getName(),
                analysisRun.getStatus().toString(),
                analysisRun.getStartDateTime(),
                analysisRun.getEndDateTime(),
                analysisRun.getCreatedDateTime()
        );
    }
}
