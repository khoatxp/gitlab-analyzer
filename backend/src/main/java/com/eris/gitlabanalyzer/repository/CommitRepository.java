package com.eris.gitlabanalyzer.repository;

import com.eris.gitlabanalyzer.model.Commit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommitRepository extends JpaRepository<Commit,Long> {
    @Query("select c from Commit c where c.sha = ?1 and c.project.id = ?2")
    Optional<Commit> findByCommitShaAndProjectId(String sha, Long projectId);

    @Query("select c from Commit c where c.project.id = ?1")
    List<Commit> findAllByProjectId(Long projectId);

    @Query("select c from Commit c where c.project.id=?1 and c.authorEmail in (select ca.authorEmail from CommitAuthor ca where ca.authorEmail = c.authorEmail and ca.gitManagementUser.id = ?2)")
    List<Commit> findByProjectIdAndGitManagementUserId(Long projectId, Long gitManagementUserId);

    @Query("select c from Commit c where c.project.id = ?1 " +
            "and c.createdAt >= ?2 and c.createdAt <= ?3 " +
            "and c.mergeRequest IS NULL " +
            "order by c.createdAt asc")
    List<Commit> findAllOrphanByProjectIdAndDateRange(Long projectId, OffsetDateTime startDateTime, OffsetDateTime endDateTime);

    @Query("select c from Commit c where c.project.id = ?1 and c.isIgnored = false and ((c.mergedAt >= ?2 and c.mergedAt <= ?3) or (c.createdAt >= ?2 and c.createdAt <= ?3 and c.mergeRequest IS NULL)) order by c.createdAt asc")
    List<Commit> findAllActiveByProjectIdAndDateRange(Long projectId, OffsetDateTime startDateTime, OffsetDateTime endDateTime);

    @Query("select c from Commit c where c.project.id = ?1 and ((c.mergedAt >= ?2 and c.mergedAt <= ?3) or (c.createdAt >= ?2 and c.createdAt <= ?3 and c.mergeRequest IS NULL)) order by c.createdAt asc")
    List<Commit> findAllByProjectIdAndDateRange(Long projectId, OffsetDateTime startDateTime, OffsetDateTime endDateTime);

    @Query("select c from Commit c where c.mergeRequest.id = ?1 and ((c.mergedAt >= ?2 and c.mergedAt <= ?3) or (c.createdAt >= ?2 and c.createdAt <= ?3 and c.mergeRequest IS NULL)) order by c.createdAt asc")
    List<Commit> findAllByMergeRequestIdAndDateRange(Long mergeRequestId, OffsetDateTime startDateTime, OffsetDateTime endDateTime);

    @Query("select c from Commit c where c.project.id = ?1 and ((c.mergedAt >= ?3 and c.mergedAt <= ?4) or (c.createdAt >= ?3 and c.createdAt <= ?4 and c.mergeRequest IS NULL)) " +
            "and c.isIgnored = false and c.authorEmail in (select ca.authorEmail from CommitAuthor ca where ca.authorEmail = c.authorEmail and ca.gitManagementUser.id = ?2) order by c.createdAt asc ")
    List<Commit> findAllActiveByProjectIdAndDateRangeAndGitManagementUserId(Long projectId, Long gitManagementUserId, OffsetDateTime startDateTime, OffsetDateTime endDateTime);

    @Query("select c from Commit c where c.project.id = ?1 and ((c.mergedAt >= ?3 and c.mergedAt <= ?4) or (c.createdAt >= ?3 and c.createdAt <= ?4 and c.mergeRequest IS NULL)) " +
            "and c.authorEmail in (select ca.authorEmail from CommitAuthor ca where ca.authorEmail = c.authorEmail and ca.gitManagementUser.id = ?2) order by c.createdAt asc ")
    List<Commit> findAllByProjectIdAndDateRangeAndGitManagementUserId(Long projectId, Long gitManagementUserId, OffsetDateTime startDateTime, OffsetDateTime endDateTime);

    @Query("select c from Commit c where c.project.id=?1 " +
            "and c.createdAt >= ?3 and c.createdAt <= ?4 " +
            "and c.mergeRequest is null and c.authorEmail in " +
            "(select ca.authorEmail from CommitAuthor ca where ca.authorEmail = c.authorEmail and ca.gitManagementUser.id = ?2) " +
            "order by c.createdAt asc")
    List<Commit> findOrphanByProjectIdAndGitManagementUserIdAndDateRange(Long projectId, Long gitManagementUserId, OffsetDateTime startDateTime, OffsetDateTime endDateTime);

    @Query("select c from Commit c where c.mergeRequest.id = ?1 and ((c.mergedAt >= ?3 and c.mergedAt <= ?4) or (c.createdAt >= ?3 and c.createdAt <= ?4 and c.mergeRequest IS NULL)) " +
            "and c.authorEmail in (select ca.authorEmail from CommitAuthor ca where ca.authorEmail = c.authorEmail and ca.gitManagementUser.id = ?2) order by c.createdAt asc ")
    List<Commit> findAllByMergeRequestIdAndDateRangeAndGitManagementUserId(Long mergeRequestId, Long gitManagementUserId, OffsetDateTime startDateTime, OffsetDateTime endDateTime);

    @Query("select c from Commit c where c.mergeRequest.id=?1 and c.isIgnored = false and c.authorEmail in (select ca.authorEmail from CommitAuthor ca where ca.authorEmail = c.authorEmail and ca.gitManagementUser.id = ?2)")
    List<Commit> findActiveByMergeIdAndGitManagementUserId(Long mergeRequestId, Long gitManagementUserId);

    @Query("select c from Commit c where c.mergeRequest.id=?1 and c.authorEmail in (select ca.authorEmail from CommitAuthor ca where ca.authorEmail = c.authorEmail and ca.gitManagementUser.id = ?2)")
    List<Commit> findByMergeIdAndGitManagementUserId(Long mergeRequestId, Long gitManagementUserId);

    List<Commit> findCommitByMergeRequest_Id(Long mergeRequestId);

    @Transactional
    @Modifying
    @Query("update Commit c set c.authorUsername = ?3 where c.authorName = ?1 and c.authorEmail = ?2 and c.project.id = ?4")
    void updateCommitAuthorUsernames(String authorName, String authorEmail, String authorUsername, Long projectId);
}
