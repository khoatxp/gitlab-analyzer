package com.eris.gitlabanalyzer.repository;

import com.eris.gitlabanalyzer.model.MergeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface MergeRequestRepository extends JpaRepository<MergeRequest, Long> {
    @Query("select m from MergeRequest m where m.iid = ?1 and m.project.id = ?2")
    MergeRequest findByIidAndProjectId(Long iid, Long projectId);

    @Query("select m from MergeRequest m where m.project.id = ?1")
    List<MergeRequest> findAllByProjectId(Long projectId);

    @Query("select m from MergeRequest m where m.project.id = ?1 and m.mergedAt >= ?2 and m.mergedAt <= ?3")
    List<MergeRequest> findAllByProjectIdAndDateRange(Long projectId, OffsetDateTime startDateTime, OffsetDateTime endDateTime);

}
