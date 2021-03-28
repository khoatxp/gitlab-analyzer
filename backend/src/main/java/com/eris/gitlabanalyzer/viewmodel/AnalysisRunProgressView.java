package com.eris.gitlabanalyzer.viewmodel;

import com.eris.gitlabanalyzer.model.AnalysisRun;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AnalysisRunProgressView {
    private final String message;
    private final Double progress;

    public static AnalysisRunProgressView fromAnalysisRun(AnalysisRun analysisRun) {
        return new AnalysisRunProgressView(
                analysisRun.getMessage(),
                analysisRun.getProgress()
        );
    }
}
