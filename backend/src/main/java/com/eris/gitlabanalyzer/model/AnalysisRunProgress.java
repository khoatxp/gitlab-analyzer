package com.eris.gitlabanalyzer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import static javax.persistence.GenerationType.SEQUENCE;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "AnalysisRunProgress")
@Table(name = "analysis_run_progress")
public class AnalysisRunProgress {
    @Id
    @SequenceGenerator(
            name = "analysis_run_progress_sequence",
            sequenceName = "analysis_run_progress_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "analysis_run_progress_sequence"
    )
    @Column(
            name = "analysis_run_progress_id"
    )
    private Long id;
    private String message;
    private Double progress;
    @JsonIgnore
    @OneToOne(mappedBy = "analysisRunProgress")
    private AnalysisRun analysisRun;

    public enum Progress {
        AtStartOfImportingMembers (0.0),
        AtStartOfImportingMergeRequests(5.0),
        AtStartOfImportingCommits (25.0),
        AtStartOfImportingOrphanCommits(70.0),
        AtStartOfImportingIssues (85.0),
        Done(100.0);

        private final Double progress;

        Progress(Double progress) {
            this.progress = progress;
        }

        public Double getValue() {
            return progress;
        }

        @Override
        public String toString() {
            return "Progress{" +
                    "progress=" + progress +
                    '}';
        }
    }
}
