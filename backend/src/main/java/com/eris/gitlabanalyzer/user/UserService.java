package com.eris.gitlabanalyzer.user;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {
    public List<User> getUsers() {
        return List.of(new User("test", "user"));
    }

}
