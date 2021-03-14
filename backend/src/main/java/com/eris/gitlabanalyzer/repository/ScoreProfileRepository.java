package com.eris.gitlabanalyzer.repository;

import com.eris.gitlabanalyzer.model.ScoreProfile;
import com.eris.gitlabanalyzer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScoreProfileRepository extends JpaRepository<ScoreProfile, Long> {
    Optional<ScoreProfile> findScoreProfileByName(String name);
}
