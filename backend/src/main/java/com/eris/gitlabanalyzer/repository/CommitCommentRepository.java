package com.eris.gitlabanalyzer.repository;

import com.eris.gitlabanalyzer.model.CommitComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;

@Repository
public interface CommitCommentRepository extends JpaRepository<CommitComment, Long> {
    @Query("select c from CommitComment c where c.gitManagementUser.username = ?1 and c.createdAt= ?2 and c.commit.sha = ?3")
    CommitComment findByUsernameAndCreatedAtAndCommitSha(String username, OffsetDateTime createdAt, String sha);
}
