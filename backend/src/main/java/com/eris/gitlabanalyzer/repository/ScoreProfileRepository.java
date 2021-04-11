package com.eris.gitlabanalyzer.repository;

import com.eris.gitlabanalyzer.model.ScoreProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScoreProfileRepository extends JpaRepository<ScoreProfile, Long> {
    Optional<ScoreProfile> findScoreProfileById(Long Id);
    List<ScoreProfile> findScoreProfilesByUserId(Long userId);

    @Query("select s from ScoreProfile s where s.user.id=?1 and s.id=?2")
    Optional<ScoreProfile> findScoreProfileByUserIdandId(Long userId, Long id);
}
