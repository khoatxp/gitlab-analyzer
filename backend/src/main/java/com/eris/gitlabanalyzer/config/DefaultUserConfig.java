//TODO This file is for debugging and to be removed later on
// when we have SSO sign in

package com.eris.gitlabanalyzer.config;

import com.eris.gitlabanalyzer.model.Server;
import com.eris.gitlabanalyzer.model.User;
import com.eris.gitlabanalyzer.repository.ServerRepository;
import com.eris.gitlabanalyzer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DefaultUserConfig {
    @Value("${gitlab.SERVER_URL}")
    String serverUrl;

    @Value("${gitlab.ACCESS_TOKEN}")
    String accessToken;
    @Bean
    CommandLineRunner commandLineRunner(ServerRepository serverRepository, UserRepository userRepository){
        return args -> {
            Server server = new Server(serverUrl);
            serverRepository.save(server);
            User user = new User("user");
            user.addServer(server, accessToken);
            userRepository.save(user);
        };
    }

}
