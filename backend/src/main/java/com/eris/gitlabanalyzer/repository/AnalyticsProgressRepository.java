package com.eris.gitlabanalyzer.repository;

import com.eris.gitlabanalyzer.model.AnalyticsProgress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnalyticsProgressRepository extends JpaRepository<AnalyticsProgress, Long> {
    AnalyticsProgress findByUserId(Long userId);
    void deleteById(Long id);
}
