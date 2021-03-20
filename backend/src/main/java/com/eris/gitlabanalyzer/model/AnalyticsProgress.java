package com.eris.gitlabanalyzer.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.time.OffsetDateTime;

import static javax.persistence.GenerationType.SEQUENCE;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "AnalyticsProgress")
@Table(name = "analytics_progress")
public class AnalyticsProgress {
    @Id
    @SequenceGenerator(
            name = "analytics_progress_sequence",
            sequenceName = "analytics_progress_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "analytics_progress_sequence"
    )
    @Column(
            name = "analytics_progress_id"
    )
    private Long id;
    private String message;
    private String progress;
    private OffsetDateTime startDateTime;
    private OffsetDateTime endDateTime;
    private Long projectId;
    private Long userId;


    public AnalyticsProgress(OffsetDateTime startDateTime, OffsetDateTime endDateTime, Long userId) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.userId = userId;
    }
}
