package com.eris.gitlabanalyzer.model;

import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.time.OffsetDateTime;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "AnalysisRun")
@Table(name = "analysis_run")
@lombok.Getter
@lombok.Setter
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


    @Column(name="score_profile_id")
    private Long scoreProfileId;

    @Column(name="score_profile_name")
    private String scoreProfileName;

    @Column(name = "start_date_time")
    private OffsetDateTime startDateTime;

    @Column(name = "end_date_time")
    private OffsetDateTime endDateTime;

    @CreationTimestamp
    @Column(name = "created_date_time")
    private OffsetDateTime createdDateTime;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    private String message;
    private Double progress;

    public AnalysisRun(
            User ownerUser,
            Project project,
            Server server,
            Status status,
            OffsetDateTime startDateTime,
            OffsetDateTime endDateTime,
            Long scoreProfileId,
            String scoreProfileName) {
        this.ownerUser = ownerUser;
        this.project = project;
        this.server = server;
        this.status = status;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.scoreProfileId = scoreProfileId;
        this.scoreProfileName = scoreProfileName;
        this.status = Status.InProgress;
        this.progress = 0.0;
        this.message = "Waiting for other projects";
    }

    public enum Status {
        InProgress ("In Progress"),
        Error ("Error"),
        Completed ("Completed");

        private final String status;

        Status(String status) {
            this.status = status;
        }

        public String toString() {
            return this.status;
        }
    }

    public enum Progress {
        AtStartOfImportingMembers(0.0),
        AtStartOfImportingMergeRequests(5.0),
        AtStartOfImportingCommits(25.0),
        AtStartOfImportingOrphanCommits(70.0),
        AtStartOfImportingIssues(85.0),
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