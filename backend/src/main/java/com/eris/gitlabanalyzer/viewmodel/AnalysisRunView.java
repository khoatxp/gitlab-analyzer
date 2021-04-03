package com.eris.gitlabanalyzer.viewmodel;

import com.eris.gitlabanalyzer.model.AnalysisRun;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisRunView {
    private Long id;
    private Long projectId;
    private String projectNameWithNamespace;
    private String projectName;
    private String status;
    private OffsetDateTime startDateTime;
    private OffsetDateTime endDateTime;
    private OffsetDateTime createdDateTime;
    private String message;
    private Double progress;

    public AnalysisRunView(String message, Double progress) {
        this.message = message;
        this.progress = progress;
    }

    public static AnalysisRunView progressFromAnalysisRun(AnalysisRun analysisRun){
        return new AnalysisRunView(
                analysisRun.getMessage(),
                analysisRun.getProgress()
        );
    }

    public static AnalysisRunView fromAnalysisRun(AnalysisRun analysisRun) {
        return new AnalysisRunView(
                analysisRun.getId(),
                analysisRun.getProject().getId(),
                analysisRun.getProject().getNameWithNamespace(),
                analysisRun.getProject().getName(),
                analysisRun.getStatus().toString(),
                analysisRun.getStartDateTime(),
                analysisRun.getEndDateTime(),
                analysisRun.getCreatedDateTime(),
                analysisRun.getMessage(),
                analysisRun.getProgress()
        );
    }
}
