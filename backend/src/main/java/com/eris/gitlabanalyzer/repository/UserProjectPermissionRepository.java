package com.eris.gitlabanalyzer.repository;

import com.eris.gitlabanalyzer.model.UserProjectPermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProjectPermissionRepository extends JpaRepository<UserProjectPermission, Long> {
}
