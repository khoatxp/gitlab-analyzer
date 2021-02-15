package com.eris.gitlabanalyzer.repository;

import com.eris.gitlabanalyzer.model.Server;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ServerRepository extends JpaRepository<Server, String> {
    @Query("select s from Server s where s.serverUrl = ?1 and s.accessToken = ?2")
    Server find(String serverUrl, String accessToken);
}

