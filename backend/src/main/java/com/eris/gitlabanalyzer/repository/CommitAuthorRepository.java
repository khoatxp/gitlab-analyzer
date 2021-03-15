package com.eris.gitlabanalyzer.repository;

import com.eris.gitlabanalyzer.model.CommitAuthor;
import com.eris.gitlabanalyzer.viewmodel.CommitAuthorView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CommitAuthorRepository extends JpaRepository<CommitAuthor, Long> {
    CommitAuthor findByAuthorNameAndAuthorEmailAndProjectId(String authorName, String authorEmail, Long projectId);

    @Query("select c.authorName as authorName, c.authorEmail as authorEmail, c.gitManagementUser.name as mappedGitManagementUserName, c.gitManagementUser.id as mappedGitManagementUserId  from CommitAuthor c where c.project.id = ?1")
    List<CommitAuthorView> findByProjectId(Long projectId);

    @Query("select c.authorName as authorName, c.authorEmail as authorEmail from CommitAuthor c where c.project.id = ?1 and c.gitManagementUser is null")
    List<CommitAuthorView> findUnmappedCommitAuthorsByProjectId(Long projectId);

    @Transactional
    @Modifying
    @Query("update CommitAuthor c set c.gitManagementUser.id = ?1 where c.authorName = ?2 and c.authorEmail = ?3 and c.project.id = ?4")
    void updateCommitAuthors(Long gitManagementUserId, String authorName, String authorEmail, Long projectId);
}
