package com.eris.gitlabanalyzer.repository;

import com.eris.gitlabanalyzer.model.UserServer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserServerRepository extends JpaRepository<UserServer, Long> {
}

