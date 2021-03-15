package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.User;
import com.eris.gitlabanalyzer.repository.ProjectRepository;
import com.eris.gitlabanalyzer.repository.UserProjectPermissionRepository;
import com.eris.gitlabanalyzer.repository.UserRepository;
import com.eris.gitlabanalyzer.repository.UserServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;


@Service
public class AuthService implements UserDetailsService {

    private UserRepository userRepository;
    private UserProjectPermissionRepository userProjectPermissionRepository;
    private UserServerRepository userServerRepository;
    private ProjectRepository projectRepository;

    @Autowired
    public AuthService(UserRepository userRepository, UserProjectPermissionRepository userProjectPermissionRepository,
                       UserServerRepository userServerRepository, ProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.userProjectPermissionRepository = userProjectPermissionRepository;
        this.userServerRepository = userServerRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findUserByUsername(username);
        User user = userOptional.orElseGet(() -> userRepository.save(new User(username)));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), "",
                true, true, true, true,
                AuthorityUtils.createAuthorityList()
        );
    }
    public User getLoggedInUser(Principal principle) {
        if (principle == null) {
            throw new AccessDeniedException("User not logged in.");
        }
        return userRepository.findUserByUsername(principle.getName()).orElseThrow(() -> new AccessDeniedException("User not found."));
    }
}
