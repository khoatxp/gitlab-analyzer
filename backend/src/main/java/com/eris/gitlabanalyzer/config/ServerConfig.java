//TODO This file is for debugging and to be removed later on

package com.eris.gitlabanalyzer.config;

import com.eris.gitlabanalyzer.model.Server;
import com.eris.gitlabanalyzer.repository.ServerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerConfig {
    @Value("${gitlab.SERVER_URL}")
    String serverUrl;

    @Value("${gitlab.ACCESS_TOKEN}")
    String accessToken;
    @Bean
    CommandLineRunner commandLineRunner(ServerRepository repository){
        return args -> {
            Server server = new Server(serverUrl, accessToken);
            repository.save(server);
        };
    }

}
