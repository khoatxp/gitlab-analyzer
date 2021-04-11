package com.eris.gitlabanalyzer.repository;

import com.eris.gitlabanalyzer.model.ScoreProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScoreProfileRepository extends JpaRepository<ScoreProfile, Long> {
    Optional<ScoreProfile> findScoreProfileById(Long Id);
}
