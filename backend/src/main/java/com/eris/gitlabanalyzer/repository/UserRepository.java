package com.eris.gitlabanalyzer.repository;

import com.eris.gitlabanalyzer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query
    User findUserByUsername(String userName);
}

