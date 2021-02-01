package eris.gitlabanalyzer.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/user")
public class UserController {
    public List<User> getUsers() {
        return List.of(new User("test", "user"));
    }
}
