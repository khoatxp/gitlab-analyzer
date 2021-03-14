package com.eris.gitlabanalyzer.controller;

import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.model.Server;
import com.eris.gitlabanalyzer.model.User;
import com.eris.gitlabanalyzer.repository.ProjectRepository;
import com.eris.gitlabanalyzer.repository.UserProjectPermissionRepository;
import org.springframework.stereotype.Service;

@Service
public class UserProjectPermissionService {

    private final UserProjectPermissionRepository userProjectPermissionRepository;
    private final ProjectRepository projectRepository;

    public UserProjectPermissionService(UserProjectPermissionRepository userProjectPermissionRepository, ProjectRepository projectRepository) {
        this.userProjectPermissionRepository = userProjectPermissionRepository;
        this.projectRepository = projectRepository;
    }

    public boolean userHasPermissionToProject(User user, Server server, Project project) {
        var permission =
                userProjectPermissionRepository.findUserProjectPermissionByUserAndServerAndProject(user, server, project);
        if (permission.isPresent()){
            return true;
        }
        return false;
    }
}
