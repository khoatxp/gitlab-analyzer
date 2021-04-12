package com.eris.gitlabanalyzer.repository;

import com.eris.gitlabanalyzer.model.MergeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MergeRequestRepository extends JpaRepository<MergeRequest, Long> {
    @Query("select m from MergeRequest m where m.iid = ?1 and m.project.id = ?2 order by m.mergedAt asc")
    Optional<MergeRequest> findByIidAndProjectId(Long iid, Long projectId);

    @Query("select m from MergeRequest m where m.project.id = ?1 order by m.mergedAt asc")
    List<MergeRequest> findAllByProjectId(Long projectId);

    @Query("select m from MergeRequest m where m.project.id = ?1 and m.mergedAt >= ?2 and m.mergedAt <= ?3 and m.isIgnored = false order by m.mergedAt asc")
    List<MergeRequest> findAllActiveByProjectIdAndDateRange(Long projectId, OffsetDateTime startDateTime, OffsetDateTime endDateTime);

    @Query("select m from MergeRequest m where m.project.id = ?1 and m.mergedAt >= ?2 and m.mergedAt <= ?3 order by m.mergedAt asc")
    List<MergeRequest> findAllByProjectIdAndDateRange(Long projectId, OffsetDateTime startDateTime, OffsetDateTime endDateTime);

    @Query("select m from MergeRequest m where m.gitManagementUser.id= ?2 and m.project.id= ?1 and m.mergedAt >= ?3 and m.mergedAt <= ?4 order by m.mergedAt asc")
    List<MergeRequest> findAllByGitManagementUserIdAndDateRange(Long projectId, Long gitManagementUserId, OffsetDateTime startDateTime, OffsetDateTime endDateTime);

    @Query("select m from MergeRequest m where m.gitManagementUser.id= ?2 and m.project.id= ?1 and m.mergedAt >= ?3 and m.mergedAt <= ?4 and m.isIgnored = false order by m.mergedAt asc")
    List<MergeRequest> findAllActiveByGitManagementUserIdAndDateRange(Long projectId, Long gitManagementUserId, OffsetDateTime startDateTime, OffsetDateTime endDateTime);

    @Query("select m from MergeRequest m where m.gitManagementUser.id= ?2 and m.project.id= ?1 and m.mergedAt >= ?3 and m.mergedAt <= ?4 and m.sharedWith is EMPTY and m.isIgnored = false order by m.mergedAt asc")
    List<MergeRequest> findAllActiveNotSharedByGitManagementUserIdAndDateRange(Long projectId, Long gitManagementUserId, OffsetDateTime startDateTime, OffsetDateTime endDateTime);

    @Query("select m from MergeRequest m where m.gitManagementUser.id= ?1 and m.project.id= ?2 and m.mergedAt >= ?3 and m.mergedAt <= ?4 and m.sharedWith is not EMPTY and m.isIgnored = false order by m.mergedAt asc")
    List<MergeRequest> findActiveOwnerSharedMergeRequests(Long gitManagementUserId, Long projectId, OffsetDateTime startDateTime, OffsetDateTime endDateTime);

    @Query("select m from MergeRequest m join m.sharedWith sw where m.project.id= ?1 and m.mergedAt >= ?3 and m.mergedAt <= ?4 and sw = ?2 and m.isIgnored = false order by m.mergedAt asc")
    List<MergeRequest> findActiveParticipantSharedMergeRequests(Long projectId, Long gitManagementUserId, OffsetDateTime startDateTime, OffsetDateTime endDateTime);

    @Query("select m from MergeRequest m join m.sharedWith sw where m.project.id= ?1 and m.mergedAt >= ?3 and m.mergedAt <= ?4 and sw = ?2 order by m.mergedAt asc")
    List<MergeRequest> findParticipantSharedMergeRequests(Long projectId, Long gitManagementUserId, OffsetDateTime startDateTime, OffsetDateTime endDateTime);

}
