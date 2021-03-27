package com.eris.gitlabanalyzer.repository;

import com.eris.gitlabanalyzer.model.MergeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface MergeRequestRepository extends JpaRepository<MergeRequest, Long> {
    @Query("select m from MergeRequest m where m.iid = ?1 and m.project.id = ?2")
    MergeRequest findByIidAndProjectId(Long iid, Long projectId);

    @Query("select m from MergeRequest m where m.project.id = ?1")
    List<MergeRequest> findAllByProjectId(Long projectId);

    @Query("select m from MergeRequest m where m.project.id = ?1 and m.mergedAt >= ?2 and m.mergedAt <= ?3")
    List<MergeRequest> findAllByProjectIdAndDateRange(Long projectId, OffsetDateTime startDateTime, OffsetDateTime endDateTime);

    @Query("select m from MergeRequest m where m.gitManagementUser.id= ?1 and m.project.id= ?2 and m.mergedAt >= ?3 and m.mergedAt <= ?4 ")
    List<MergeRequest> findAllByGitManagementUserIdAndDateRange(Long gitManagementUserId, Long projectId, OffsetDateTime startDateTime, OffsetDateTime endDateTime);

    @Query("select m from MergeRequest m where m.gitManagementUser.id= ?1 and m.project.id= ?2 and m.mergedAt >= ?3 and m.mergedAt <= ?4 and m.sharedWith is EMPTY ")
    List<MergeRequest> findAllNotSharedByGitManagementUserIdAndDateRange(Long gitManagementUserId, Long projectId, OffsetDateTime startDateTime, OffsetDateTime endDateTime);

    @Query("select m from MergeRequest m where m.gitManagementUser.id= ?1 and m.project.id= ?2 and m.mergedAt >= ?3 and m.mergedAt <= ?4 and m.sharedWith is not EMPTY ")
    List<MergeRequest> findOwnerSharedMergeRequests(Long gitManagementUserId, Long projectId, OffsetDateTime startDateTime, OffsetDateTime endDateTime);

    @Query("select m from MergeRequest m join m.sharedWith sw where m.project.id= ?1 and m.mergedAt >= ?3 and m.mergedAt <= ?4 and sw = ?2")
    List<MergeRequest> findParticipantSharedMergeRequests(Long projectId, Long gitManagementUserId, OffsetDateTime startDateTime, OffsetDateTime endDateTime);

}
