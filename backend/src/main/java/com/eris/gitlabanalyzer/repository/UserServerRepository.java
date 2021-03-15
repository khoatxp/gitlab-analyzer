package com.eris.gitlabanalyzer.repository;

import com.eris.gitlabanalyzer.model.UserServer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserServerRepository extends JpaRepository<UserServer, Long> {
    List<UserServer> findUserServerByUserId(Long userId);
    Optional<UserServer> findUserServerByAccessToken(String accessToken);
    @Query("select u from UserServer u where u.user.id = ?1 and u.server.id = ?2")
    Optional<UserServer> findUserServerByUserIdAndProjectId(Long userId, Long serverId);
}

