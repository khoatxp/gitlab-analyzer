package com.eris.gitlabanalyzer.repository;

import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.model.Server;
import com.eris.gitlabanalyzer.model.User;
import com.eris.gitlabanalyzer.model.UserProjectPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProjectPermissionRepository extends JpaRepository<UserProjectPermission, Long> {
    List<UserProjectPermission> findUserProjectPermissionByUser(User user);
    List<UserProjectPermission> findUserProjectPermissionByUserAndServer(User user, Server server);
    Optional<UserProjectPermission> findUserProjectPermissionByUserAndServerAndProject(User user, Server server, Project project);
}
