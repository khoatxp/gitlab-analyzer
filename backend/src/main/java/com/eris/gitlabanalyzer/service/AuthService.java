package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.User;
import com.eris.gitlabanalyzer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class AuthService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
