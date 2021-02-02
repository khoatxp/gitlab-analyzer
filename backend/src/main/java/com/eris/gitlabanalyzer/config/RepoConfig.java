package com.eris.gitlabanalyzer.config;

import com.eris.gitlabanalyzer.model.Repo;
import com.eris.gitlabanalyzer.repository.RepoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepoConfig {
    @Bean
    CommandLineRunner commandLineRunner(RepoRepository repository) {
        return args -> {
//            Repo test = new Repo(
//                    "test"
//            );
//            repository.save(test);
        };
    }
}
