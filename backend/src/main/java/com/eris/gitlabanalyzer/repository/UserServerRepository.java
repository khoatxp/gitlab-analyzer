package com.eris.gitlabanalyzer.repository;

import com.eris.gitlabanalyzer.model.Server;
import com.eris.gitlabanalyzer.model.UserServer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserServerRepository extends JpaRepository<UserServer, Long> {
    List<UserServer> findUserServerByUserId(Long userId);
}

