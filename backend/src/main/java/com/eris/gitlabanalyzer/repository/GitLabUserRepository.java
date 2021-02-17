package com.eris.gitlabanalyzer.repository;
import com.eris.gitlabanalyzer.model.GitLabUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GitLabUserRepository extends JpaRepository<GitLabUser, Long>{
    @Query("select g from GitLabUser g inner join g.projects project where project.id = ?1")
    List<GitLabUser> findByProjectId(Long projectId);

    @Query("select g from GitLabUser g where g.username = ?1")
    GitLabUser findByUserNameAndServerUrl(String username, String serverUrl);
}
