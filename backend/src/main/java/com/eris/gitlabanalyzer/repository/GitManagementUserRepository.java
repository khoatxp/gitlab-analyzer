package com.eris.gitlabanalyzer.repository;
import com.eris.gitlabanalyzer.model.GitManagementUser;
import com.eris.gitlabanalyzer.viewmodel.GitManagementUserView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GitManagementUserRepository extends JpaRepository<GitManagementUser, Long>{
    @Query("select g from GitManagementUser g inner join g.projects project where g.gitLabUserId = ?1 and project.id = ?2")
    GitManagementUser findByGitLabUserIdAndProjectId(Long gitLabUserId, Long projectId);

    @Query("select g from GitManagementUser g where g.username = ?1 and g.server.id = ?2")
    GitManagementUser findByUsernameAndServerId(String username, Long serverId);

    @Query("select g from GitManagementUser g where g.gitLabUserId = ?1 and g.server.id = ?2")
    GitManagementUser findByGitLabUserIdAndServerId(Long gitLabUserId, Long serverId);

    @Query("select g.id as id, g.username as username, g.name as name from GitManagementUser g inner join g.projects project where project.id = ?1 order by g.username asc")
    List<GitManagementUserView> findByProjectId(Long projectId);

    @Query("select g.id as id, g.username as username, g.name as name from GitManagementUser g where g.id = ?1")
    GitManagementUserView findByGitManagementUserId(Long gitManagementUserId);
}
