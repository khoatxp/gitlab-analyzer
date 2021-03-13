package com.eris.gitlabanalyzer.repository;

import com.eris.gitlabanalyzer.model.Server;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ServerRepository extends JpaRepository<Server, Long> {
    // TODO use serverId instead of serverUrl
    @Query("select s from Server s join s.userServers us where s.serverUrl=?1 and us.accessToken=?2")
    Optional<Server> findByServerUrlAndAccessToken(String serverUrl, String accessToken);

    @Query("select s from Server s join s.userServers us where s.serverUrl=?1 and us.user.id=?2")
    Optional<Server> findByServerUrlAndUserId(String serverUrl, Long userId);

    Optional<Server> findByServerUrl(String serverUrl);
}

