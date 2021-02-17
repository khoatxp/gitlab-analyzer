package com.eris.gitlabanalyzer.repository;

import com.eris.gitlabanalyzer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}

