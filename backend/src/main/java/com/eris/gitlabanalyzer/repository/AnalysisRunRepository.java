package com.eris.gitlabanalyzer.repository;

import com.eris.gitlabanalyzer.model.AnalysisRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnalysisRunRepository extends JpaRepository<AnalysisRun, Long> {
    List<AnalysisRun> findByOwnerUserIdAndServerIdOrderByCreatedDateTimeDesc(Long userId, Long serverId);

    @Query("SELECT a FROM AnalysisRun a WHERE a.ownerUser.id <> ?1 AND a.server.id = ?2 AND a.project.gitLabProjectId IN ?3")
    List<AnalysisRun> findOthersByServerIdAndGitLabProjectIds(Long userId, Long serverId, List<Long> gitlabProjectIds);

    @Query("SELECT a FROM AnalysisRun a WHERE a.id IN ?1")
    List<AnalysisRun> findByIds(List<Long> analysisRunIds);
}
