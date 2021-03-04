package com.eris.gitlabanalyzer.repository;
import com.eris.gitlabanalyzer.model.GitManagementUser;
import com.eris.gitlabanalyzer.model.frontendresponse.GitManagementUserResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GitManagementUserRepository extends JpaRepository<GitManagementUser, Long>{
    @Query("select g from GitManagementUser g inner join g.projects project where project.id = ?1")
    List<GitManagementUser> findByProjectId(Long projectId);

    @Query("select g from GitManagementUser g inner join g.projects project where g.username = ?1 and project.id = ?2")
    GitManagementUser findByUsernameAndProjectId(String username, Long projectId);

    @Query("select g from GitManagementUser g where g.username = ?1 and g.server.serverUrl = ?2")
    GitManagementUser findByUserNameAndServerUrl(String username, String serverUrl);

    @Query("select g.id as id, g.username as username, g.name as name from GitManagementUser g inner join g.projects project where project.id = ?1 order by g.name asc")
    List<GitManagementUserResponse> getByProjectId(Long projectId);
}
