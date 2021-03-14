package com.eris.gitlabanalyzer.repository;

import com.eris.gitlabanalyzer.model.Commit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface CommitRepository extends JpaRepository<Commit,Long> {
    @Query("select c from Commit c where c.sha = ?1 and c.project.id = ?2")
    Commit findByCommitShaAndProjectId(String sha, Long projectId);

    @Query("select c from Commit c where c.project.id = ?1")
    List<Commit> findAllByProjectId(Long projectId);

    @Query("select c from Commit c where c.project.id = ?1 and c.mergedAt >= ?2 and c.mergedAt <= ?3")
    List<Commit> findAllByProjectIdAndDateRange(Long projectId, OffsetDateTime startDateTime, OffsetDateTime endDateTime);

    @Query("select c from Commit c where c.project.id = ?1 and c.createdAt >= ?2 and c.createdAt <= ?3 and c.mergeRequest  IS NULL")
    List<Commit> findAllOrphanByProjectIdAndDateRange(Long projectId, OffsetDateTime startDateTime, OffsetDateTime endDateTime);

}
