package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.model.Server;
import com.eris.gitlabanalyzer.model.User;
import com.eris.gitlabanalyzer.repository.UserProjectPermissionRepository;
import com.eris.gitlabanalyzer.repository.UserRepository;
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

    public AuthService(UserRepository userRepository, UserProjectPermissionRepository userProjectPermissionRepository) {
        this.userRepository = userRepository;
        this.userProjectPermissionRepository = userProjectPermissionRepository;
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

    public boolean hasProjectPermission(Long userId, Long serverId, Long projectId) {
        return userProjectPermissionRepository.findByUserIdAndServerIdAndProjectId(
                userId, serverId, projectId).isPresent();
    }

}
