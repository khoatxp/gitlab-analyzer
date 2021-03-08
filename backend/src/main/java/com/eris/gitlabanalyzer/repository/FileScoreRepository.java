package com.eris.gitlabanalyzer.repository;
import com.eris.gitlabanalyzer.model.FileScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileScoreRepository extends JpaRepository<FileScore, Long> {
    @Query("select f from FileScore f where f.mergeRequest.id = ?1")
    List<FileScore> findByMergeId(Long mergeId);

    @Query("select f from FileScore f where f.commit.id = ?1")
    List<FileScore> findByCommitId(Long commitId);
}
