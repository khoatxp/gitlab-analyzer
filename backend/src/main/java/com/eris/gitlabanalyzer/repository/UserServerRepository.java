package com.eris.gitlabanalyzer.repository;

import com.eris.gitlabanalyzer.model.UserServer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserServerRepository extends JpaRepository<UserServer, Long> {

    Optional<UserServer> findUserServerByAccessToken(String accessToken);
}
