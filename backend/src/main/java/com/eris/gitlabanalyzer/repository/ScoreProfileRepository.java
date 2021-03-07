package com.eris.gitlabanalyzer.repository;

import com.eris.gitlabanalyzer.model.ScoreProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreProfileRepository extends JpaRepository<ScoreProfile, Long> {

}
