package com.eris.gitlabanalyzer.repository;

import com.eris.gitlabanalyzer.model.Commit;
import com.eris.gitlabanalyzer.viewmodel.CommitAuthorView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CommitRepository extends JpaRepository<Commit,Long> {
    @Query("select c from Commit c where c.sha = ?1 and c.project.id = ?2")
    Commit findByCommitShaAndProjectId(String sha, Long projectId);

    @Query("select c from Commit c where c.project.id = ?1")
    List<Commit> findAllByProjectId(Long projectId);

    @Transactional
    @Modifying
    @Query("update Commit c set c.gitManagementUser.id = ?1 where c in (select c2 from Commit c2 where c2.authorName= ?2 and c2.authorEmail=?3 and c2.project.id = ?4)")
    void updateCommitAuthors(Long gitManagementUserId, String authorName, String authorEmail, Long projectId);
}
