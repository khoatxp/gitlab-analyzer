package com.eris.gitlabanalyzer.repo;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RepoService {
    public List<Repo> getRepos() {
        return List.of(new Repo("test", "test"));
    }
}
