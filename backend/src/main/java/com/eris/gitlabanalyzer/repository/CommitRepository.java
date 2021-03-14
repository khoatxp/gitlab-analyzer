package com.eris.gitlabanalyzer.repository;

import com.eris.gitlabanalyzer.model.Commit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommitRepository extends JpaRepository<Commit,Long> {
    @Query("select c from Commit c where c.sha = ?1 and c.project.id = ?2")
    Commit findByCommitShaAndProjectId(String sha, Long projectId);

    @Query("select c from Commit c where c.project.id = ?1")
    List<Commit> findAllByProjectId(Long projectId);

    @Query("select c from Commit c where c.project.id=?1 and c.authorEmail in (select ca.authorEmail from CommitAuthor ca where ca.authorEmail = c.authorEmail and ca.gitManagementUser.id = ?2)")
    List<Commit> findByProjectIdAndGitManagementUserId(Long projectId, Long gitManagementUserId);
}
