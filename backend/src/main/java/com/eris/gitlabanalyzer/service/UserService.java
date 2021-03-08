package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.User;
import com.eris.gitlabanalyzer.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addUser(String userName) {
        User newUser = new User(userName);
        userRepository.save(newUser);
    }

    public Long getUserLongId(String userName) {
        Optional<User> user = userRepository.findUserByUsername(userName);
        if (user.isEmpty()){
            // Our Sequence for User Id start from 1L. So 0L never exist in our database.
            // For now, I'm using 0L as a signal that the user does not exist.
            return 0L;
        } else {
            return user.get().getId();
        }
    }
}
