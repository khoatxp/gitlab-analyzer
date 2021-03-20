package com.eris.gitlabanalyzer.model;

import javax.persistence.*;
import java.time.OffsetDateTime;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "AnalysisRun")
@Table(name = "analysis_run")
@lombok.Getter
@lombok.NoArgsConstructor
public class AnalysisRun {
    @Id
    @SequenceGenerator(
            name = "analysis_run_sequence",
            sequenceName = "analysis_run_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "analysis_run_sequence"
    )
    @Column(name = "analysis_run_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User ownerUser;

    @ManyToOne
    @JoinColumn(name="project_id")
    private Project project;

    // TODO: Hookup once score profile implemented
//    @ManyToOne
//    @JoinColumn(name="id")
//    private ScoreProfile scoreProfile;

    @Column
    private OffsetDateTime startDateTime;

    @Column
    private OffsetDateTime endDate;

    public AnalysisRun(User ownerUser, Project project, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        this.ownerUser = ownerUser;
        this.project = project;
//        this.scoreProfile = scoreProfile; TODO: hookup once implemented
        this.startDateTime = startDateTime;
        this.endDate = endDateTime;
    }
}