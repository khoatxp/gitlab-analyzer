package com.eris.gitlabanalyzer.repository;
import com.eris.gitlabanalyzer.model.FileScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileScoreRepository extends JpaRepository<FileScore, Long> {
    @Query("select f from FileScore f where f.project.id = ?1 and f.mergeRequest.iid = ?2")
    List<FileScore> findByProjectIdAndMergeId(Long projectId, Long mergeId);

    @Query("select f from FileScore f where f.project.id = ?1 and f.commit.sha = ?2")
    List<FileScore> findByProjectIdAndCommitSha(Long projectId, String commitSha);
}
