package com.eris.gitlabanalyzer.model;

import org.hibernate.annotations.CreationTimestamp;

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

    @ManyToOne
    @JoinColumn(name = "server_id")
    private Server server;

    // TODO: Hookup once score profile implemented
//    @ManyToOne
//    @JoinColumn(name="id")
//    private ScoreProfile scoreProfile;

    @Column(name = "start_date_time")
    private OffsetDateTime startDateTime;

    @Column(name = "end_date_time")
    private OffsetDateTime endDateTime;

    @CreationTimestamp
    @Column(name = "created_date_time")
    private OffsetDateTime createdDateTime;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AnalysisRunStatus status;

    public AnalysisRun(User ownerUser, Project project, Server server, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        this.ownerUser = ownerUser;
        this.project = project;
        this.server = server;
//        this.scoreProfile = scoreProfile; TODO: hookup once implemented
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.status = AnalysisRunStatus.InProgress;
    }

    public enum AnalysisRunStatus {
        InProgress ("In Progress"),
        Error ("Error"),
        Completed ("Completed");

        private final String status;

        AnalysisRunStatus(String status) {
            this.status = status;
        }

        public String toString() {
            return this.status;
        }
    }
}