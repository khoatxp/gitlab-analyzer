package com.eris.gitlabanalyzer.viewmodel;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class ScoreDigest {
    private Double commitScore;
    private Double mergeRequestScore;
    private int commitCount;
    private int mergeRequestCount;
    private LocalDate day;
}
