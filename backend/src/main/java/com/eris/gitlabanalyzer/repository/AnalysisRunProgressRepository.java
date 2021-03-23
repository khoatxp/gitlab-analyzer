package com.eris.gitlabanalyzer.repository;

import com.eris.gitlabanalyzer.model.AnalysisRunProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface AnalysisRunProgressRepository extends JpaRepository<AnalysisRunProgress, Long> {
    @Query("select p from AnalysisRunProgress p where p.analysisRun.id =?1")
    Optional<AnalysisRunProgress> findByAnalysisRunId(Long analysisRunId);
}
