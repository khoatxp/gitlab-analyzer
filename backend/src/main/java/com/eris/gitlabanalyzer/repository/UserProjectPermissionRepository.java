package com.eris.gitlabanalyzer.repository;

import com.eris.gitlabanalyzer.model.UserProjectPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Optional;

@Repository
public interface UserProjectPermissionRepository extends JpaRepository<UserProjectPermission, Long> {
    @Query("select p from UserProjectPermission p where p.user.id = ?1 and p.server.id = ?2 and p.project.id = ?3")
    Optional<UserProjectPermission> findByUserIdAndServerIdAndProjectId(Long userId, Long serverId, Long projectId);

    @Query("select p.project.id from UserProjectPermission p where p.user.id = ?1 and p.server.id = ?2")
    HashSet<Long> findProjectIdsByUserIdAndServerId(Long userId, Long serverId);
}
